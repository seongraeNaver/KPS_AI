package com.kimparksin.meatplus.service;

import com.kimparksin.meatplus.dto.UserDto;
import com.kimparksin.meatplus.entity.User;
import com.kimparksin.meatplus.exception.ResourceNotFoundException;
import com.kimparksin.meatplus.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //사용자 데이터를 받아 데이터베이스에 저장하고, 저장된 데이터를 반환합니다.
    public UserDto createUser(UserDto userDto) {
        log.info("Creating new user with email: {}", userDto.getEmail());
        User user = userDto.toEntity();
        User savedUser = userRepository.save(user);
        return new UserDto(savedUser);
    }

    //데이터베이스에서 모든 사용자를 조회하고, 이를 DTO 리스트로 반환합니다
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    //사용자 ID로 특정 사용자를 조회.
    public UserDto getUserById(Long id) {
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return new UserDto(foundUser);
    }

    //현재 인증된 사용자의 이메일을 기반으로 사용자 정보를 조회.
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    //클라이언트로부터 전달받은 데이터를 사용하여 새로운 사용자를 등록
    public void registerUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPwd(passwordEncoder.encode(userDto.getPwd())); // 암호화
        user.setRole("USER");
        user.setBirthdate(userDto.getBirthdate());
        user.setAddress(userDto.getAddress());
        userRepository.save(user);
    }

//    //주어진 평문 비밀번호를 암호화하여 반환
//    private String encryptPassword(String rawPassword) {
//        return passwordEncoder.encode(rawPassword);
//    }


}
