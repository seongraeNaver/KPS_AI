package com.kimparksin.meatplus.service;

import com.kimparksin.meatplus.entity.User;
import com.kimparksin.meatplus.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

//UserRepository를 통해 현재 인증된 사용자의 이메일로 DB에서 User 엔티티를 조회하는 간단한 메서드를 작성합니다.
@Service
public class AuthHelperService {

    private final UserRepository userRepository;

    public AuthHelperService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getAuthenticatedUser(Authentication authentication) {
        String email = authentication.getName(); // 현재 인증된 사용자 이메일 가져오기
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found for email: " + email));
    }
}
