package com.kimparksin.meatplus.controller;

import com.kimparksin.meatplus.dto.UserDto;
import com.kimparksin.meatplus.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // 로그인 폼 렌더링
    @GetMapping("/auth/login-form")
    public String loginForm() {
        return "login"; // login.html 반환
    }

    // 회원가입 폼 렌더링
    @GetMapping("/auth/register")
    public String registerForm(Model model) {
        model.addAttribute("userDto", new UserDto()); // 빈 UserDto 객체를 모델에 추가
        return "register"; // register.html 뷰를 반환
    }

    // 회원가입 처리
    @PostMapping("/auth/register")
    public String register(UserDto userDto) {
        userService.registerUser(userDto);
        return "redirect:/auth/login-form"; // 회원가입 후 로그인 페이지로 이동
    }

}
