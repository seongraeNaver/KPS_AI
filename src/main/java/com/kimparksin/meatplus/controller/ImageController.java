package com.kimparksin.meatplus.controller;

import com.kimparksin.meatplus.dto.ImageDto;
import com.kimparksin.meatplus.dto.ProjectDto;
import com.kimparksin.meatplus.entity.Image;
import com.kimparksin.meatplus.entity.Project;
import com.kimparksin.meatplus.entity.User;
import com.kimparksin.meatplus.repository.ProjectRepository;
import com.kimparksin.meatplus.service.AuthHelperService;
import com.kimparksin.meatplus.service.ImageProcessingService;
import com.kimparksin.meatplus.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class ImageController {

    private final ImageProcessingService imageProcessingService;
    private final ProjectService projectService;
    private final AuthHelperService authHelperService;

    public ImageController(ImageProcessingService imageProcessingService, ProjectService projectService, AuthHelperService authHelperService) {
        this.imageProcessingService = imageProcessingService;
        this.projectService = projectService;
        this.authHelperService = authHelperService;
    }

    //홈 페이지 요청 - 클라이언트가 / URL로 접근하면 **index.html**을 렌더링합니다.
    @GetMapping("/")
    public String mainPage(Model model, Authentication authentication) {
        model.addAttribute("streamUrl", "http://localhost:5000/video_feed");

        // 로그인된 사용자 정보 추가
        if (authentication != null && authentication.isAuthenticated()) {
            String userEmail = authentication.getName(); // 로그인된 사용자 이메일 가져오기
            model.addAttribute("userName", userEmail);   // 사용자 이메일을 모델에 추가
            model.addAttribute("isLoggedIn", true);      // 로그인 상태 추가

            // 사용자의 프로젝트 목록 조회
            List<ProjectDto> userProjects = projectService.getProjectsByUserEmail(userEmail);
            model.addAttribute("projects", userProjects); // 프로젝트 목록 추가
        } else {
            model.addAttribute("isLoggedIn", false);      // 비로그인 상태 추가
        }

        return "index"; // index.html 반환
    }


    // 이미지 처리 요청 (업로드된 원본 이미지를 AI 서버로 전송해 결과를 반환)
    @PostMapping("/api/process-image")
    public String processImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("projectId") Long projectId,
            Authentication authentication,
            Model model) {
        try {
            // 로그인된 사용자 이메일 가져오기
            String userEmail = authentication.getName();

            // 서비스 호출로 비즈니스 로직 위임
            Map<String, String> imageUrls = imageProcessingService.processImage(file, projectId, userEmail);

            // 처리 결과를 모델에 추가하여 뷰로 전달
            model.addAttribute("originalImageUrl", imageUrls.get("original"));
            model.addAttribute("resultImageUrl", imageUrls.get("processed"));
            return "result"; // 결과 페이지 반환
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "이미지 처리 중 오류가 발생했습니다.");
            return "result"; // 에러 메시지와 함께 결과 뷰 반환
        }
    }


    //실시간 스트리밍 할때 사용
    @PostMapping("/api/upload-captured-images")
    public ResponseEntity<String> uploadCapturedImages(
            @RequestParam("originalImage") MultipartFile originalImage,
            @RequestParam("processedImage") MultipartFile processedImage,
            @RequestParam("projectId") Long projectId,
            Authentication authentication) {
        try {
            // 사용자 이메일 가져오기
            String userEmail = authentication.getName();

            // 원본 이미지와 검출 이미지 확인
            if (originalImage.isEmpty() || processedImage.isEmpty()) {
                return ResponseEntity.badRequest().body("원본 또는 검출 이미지가 누락되었습니다.");
            }

            // 서비스 계층으로 전달
            imageProcessingService.processCapturedImages(originalImage, processedImage, projectId, userEmail);

            return ResponseEntity.ok("이미지 업로드 성공");
        } catch (Exception e) {
            log.error("Error uploading captured images: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("이미지 업로드 실패");
        }
    }


    //사용자가 자신의 업로드 이미지를 확인??
    @GetMapping("/user-images")
    public String viewUserImages() {
        return "user_images";
    }


    // 단일 목적 페이지: 오직 스트리밍 전용으로 설계.
    @GetMapping("/stream")
    public String stream(Model model) {
        model.addAttribute("streamUrl", "http://localhost:5000/video_feed");
        return "stream";
    }


    // 로그인된 사용자의 이미지를 조회
    @GetMapping("/user/images")
    public ResponseEntity<List<Image>> getUserImages(Authentication authentication) {
        String email = authentication.getName(); // 로그인된 사용자의 이메일
        List<Image> images = imageProcessingService.getImagesByUserEmail(email);
        return ResponseEntity.ok(images);
    }


    // 현재 사용자의 이미지 조회
    @GetMapping("/images")
    public ResponseEntity<List<Image>> getCurrentUserImages() {
        List<Image> images = imageProcessingService.getImagesForCurrentUser();
        return ResponseEntity.ok(images);
    }


}
