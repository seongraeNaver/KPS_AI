package com.kimparksin.meatplus.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kimparksin.meatplus.dto.ImageDto;
import com.kimparksin.meatplus.dto.ProjectDto;
import com.kimparksin.meatplus.entity.Image;
import com.kimparksin.meatplus.entity.Project;
import com.kimparksin.meatplus.entity.User;
import com.kimparksin.meatplus.exception.CustomException;
import com.kimparksin.meatplus.repository.ImageRepository;
import com.kimparksin.meatplus.repository.ProjectRepository;
import com.kimparksin.meatplus.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImageProcessingService {

    private final WebClient webClient;
    private final S3Service s3Service;
    private final UserService userService;
    private final ImageRepository imageRepository; // ImageRepository 추가
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;


    public ImageProcessingService(WebClient.Builder webClientBuilder, S3Service s3Service, UserService userService, ImageRepository imageRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:5000").build();
        this.s3Service = s3Service;
        this.userService = userService;
        this.imageRepository = imageRepository; // 의존성 주입
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    // 클라이언트가 업로드한 이미지에서 AI 처리 및 S3 업로드까지 전체 워크플로우를 수행
    // 이미지 업로드 할 때 사용되는 메서드
    public Map<String, String> processImage(MultipartFile file, Long projectId, String userEmail) {
        try {
            log.info("Starting image processing workflow.");

            // 사용자와 프로젝트 검증
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
            Project project = projectRepository.findByProjectCodeAndUser_Id(projectId, user.getId())
                    .orElseThrow(() -> new RuntimeException("Invalid project for user: " + userEmail));

            String timestamp = getCurrentTimestamp();
            String uniqueID = generateSHA1UUID(file.getOriginalFilename() + timestamp);

            // Step 1: Upload original image to S3
            log.info("Uploading original image to S3...");
            String originalFileName = "original_" + timestamp + "_" + uniqueID + "_" + file.getOriginalFilename();
            String originalFileUrl = s3Service.uploadFile(file, originalFileName);

            // Step 2: Save input time (이미지 업로드 시점) - 이미지 업로드 직후(원본 이미지를 S3에 저장한 후) inputTime
            LocalDateTime inputTime = LocalDateTime.now();

            // Step 3: Process image via AI server
            log.info("Sending image to AI server for processing...");
            Mono<String> responseMono = webClient.post()
                    .uri("/run_yolov5")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData("file", file.getResource()))
                    .retrieve()
                    .bodyToMono(String.class);

            String jsonResponse = responseMono.block();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);

            if (!jsonNode.has("result_image")) {
                throw new CustomException("Invalid response from AI server. Missing 'result_image' field.");
            }

            // Step 4: Download processed image and upload to S3
            log.info("Downloading processed image from AI server...");
            String resultImageName = jsonNode.get("result_image").asText();
            String resultFileUrl = downloadAndUploadResultImage(resultImageName, timestamp, uniqueID, file.getOriginalFilename());

            // Step 5: Save output time (AI 처리 완료 시점) - AI 서버에서 처리 결과가 반환된 후(결과 이미지를 S3에 저장한 후) outputTime
            LocalDateTime outputTime = LocalDateTime.now();

            // Step 6: Save image record to database
            log.info("Saving processed image record to database...");
            Image image = new Image();
            image.setInputImg(originalFileUrl);
            image.setOutputImg(resultFileUrl);
            image.setInputTime(inputTime); // 업로드 시점
            image.setOutputTime(outputTime); // AI 처리 완료 시점
            image.setProject(project); // 프로젝트 정보 연결
            image.setUser(user); // 사용자 정보 연결

            imageRepository.save(image);

            Map<String, String> urls = new HashMap<>();
            urls.put("original", originalFileUrl);
            urls.put("processed", resultFileUrl);

            log.info("Image processing workflow completed successfully.");
            return urls;

        } catch (Exception e) {
            log.error("Error during image processing workflow: {}", e.getMessage(), e);
            throw new CustomException("An error occurred during image processing.", e);
        }
    }


    //실시간 웹캠 작동할 때 사용되는 메서드
    public void processCapturedImages(
            MultipartFile originalImage,
            MultipartFile processedImage,
            Long projectId,
            String userEmail) {
        try {
            log.info("Processing captured images for project {}", projectId);

            // Step 1: 사용자 및 프로젝트 정보 검증
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
            Project project = projectRepository.findByProjectCodeAndUser_Id(projectId, user.getId())
                    .orElseThrow(() -> new RuntimeException("Invalid project for user: " + userEmail));
            log.info("User and Project validated successfully.");

            // Step 2: S3 업로드
            String timestamp = getCurrentTimestamp();
            String originalFileName = "project_" + "_original_" + timestamp + ".jpg";
            String processedFileName = "project_" + "_processed_" + timestamp + ".jpg";

            String originalFileUrl = s3Service.uploadFile(originalImage.getInputStream(), originalFileName, "image/jpeg");
            String processedFileUrl = s3Service.uploadFile(processedImage.getInputStream(), processedFileName, "image/jpeg");

            log.info("S3 Upload completed: Original - {}, Processed - {}", originalFileUrl, processedFileUrl);

            // Step 3: DB 저장
            Image image = new Image();
            image.setInputImg(originalFileUrl);
            image.setOutputImg(processedFileUrl);
            image.setInputTime(LocalDateTime.now());
            image.setOutputTime(LocalDateTime.now());
            image.setUser(user);
            image.setProject(project);
            imageRepository.save(image);
            log.info("Image record saved to database for project {}", projectId);

        } catch (Exception e) {
            log.error("Error during image processing: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process captured images.", e);
        }
    }

    // AI 서버에서 처리된 결과 이미지를 다운로드한 뒤, AWS S3에 업로드하고 해당 이미지의 S3 URL을 반환하는 역할을(prossImage 메서드에서 사용됨)
    private String downloadAndUploadResultImage(String resultImageName, String timestamp, String uniqueID, String originalFileName) {
        try {
            byte[] resultImageBytes = webClient.get()
                    .uri("/result/" + resultImageName)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();

            if (resultImageBytes == null) {
                throw new CustomException("Failed to download result image from AI server.");
            }

            String resultFileName = "processed_" + timestamp + "_" + uniqueID + "_" + originalFileName;
            return s3Service.uploadFile(new ByteArrayInputStream(resultImageBytes), resultFileName, "image/jpeg");

        } catch (Exception e) {
            log.error("Error during downloading/uploading result image: {}", e.getMessage(), e);
            throw new CustomException("An error occurred while processing the result image.", e);
        }
    }


    // 특정 프로젝트에 속한 이미지 목록 조회
    public List<ImageDto> getImagesByProjectCode(Long projectCode) {
        // 이미지 리스트를 DB에서 조회하고 DTO로 변환
        return imageRepository.findByProject_ProjectCode(projectCode).stream()
                .map(ImageDto::new) // DTO로 변환
                .collect(Collectors.toList());
    }

    // 현재 사용자에 대한 모든 이미지 조회
    public List<ImageDto> getUserImages() {
        User currentUser = userService.getCurrentUser();
        List<Image> images = imageRepository.findByUser(currentUser);
        return images.stream()
                .map(ImageDto::new) // Image 엔티티를 ImageDto로 변환
                .collect(Collectors.toList());
    }


    // 현재 사용자 관련 이미지 조회
    public List<Image> getImagesForCurrentUser() {
        // 현재 로그인된 사용자 조회
        User currentUser = userService.getCurrentUser();
        log.info("Fetching images for user: {}", currentUser.getEmail());

        // Repository에서 사용자 ID 기반으로 이미지 가져오기
        return imageRepository.findByUserId(currentUser.getId());
    }


    public List<Image> getImagesByUserEmail(String email) {
        // 이메일로 사용자 엔티티 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // 사용자와 관련된 이미지를 반환
        return imageRepository.findByUser(user);
    }


    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }


    private String generateSHA1UUID(String input) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] hash = sha1.digest(input.getBytes());
        return UUID.nameUUIDFromBytes(hash).toString();
    }
}


