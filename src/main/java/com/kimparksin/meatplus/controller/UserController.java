package com.kimparksin.meatplus.controller;

import com.kimparksin.meatplus.dto.ProjectDto;
import com.kimparksin.meatplus.dto.UserDto;
import com.kimparksin.meatplus.entity.User;
import com.kimparksin.meatplus.service.ProjectService;
import com.kimparksin.meatplus.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ProjectService projectService;

    public UserController(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;

    }

    //클라이언트가 사용자 데이터를 JSON 형식으로 전송하면 새로운 사용자를 생성
    //주로 RESTful 서비스에서 API를 통해 사용자 등록을 처리하기 위해 사용됩니다.
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }


    //모든 사용자의 정보를 JSON으로 반환.
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    //경로 변수(id)를 사용해 특정 사용자를 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


    // 마이페이지
    @GetMapping("/mypage")
    public String myPage(Model model, Authentication authentication) {
        String userEmail = authentication.getName(); // 로그인된 사용자 이메일 가져오기
        User currentUser = userService.getCurrentUser(); // 사용자 객체 가져오기

        // 사용자와 연관된 프로젝트 조회
        List<ProjectDto> userProjects = projectService.getProjectsByUserEmail(userEmail);

        // 모델에 데이터 추가
        model.addAttribute("projects", userProjects); // 프로젝트 목록 전달
        model.addAttribute("isLoggedIn", true); // 로그인 상태 전달
        model.addAttribute("userName", currentUser.getName()); // 사용자 이름 전달
        model.addAttribute("email", currentUser.getEmail()); // 사용자 이메일 전달
        model.addAttribute("address", currentUser.getAddress()); // 사용자 주소 전달
        model.addAttribute("birthdate", currentUser.getBirthdate()); // 사용자 생년월일 전달

        return "mypage"; // Thymeleaf 템플릿 렌더링
    }

}
