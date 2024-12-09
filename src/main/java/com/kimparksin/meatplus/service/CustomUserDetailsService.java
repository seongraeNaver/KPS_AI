package com.kimparksin.meatplus.service;

import com.kimparksin.meatplus.entity.User;
import com.kimparksin.meatplus.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

//CustomUserDetailsService는 Spring Security에서 인증(Authentication)을 처리하기 위해 사용하는 서비스 클래스입니다.
//이 클래스는 스프링 시큐리티가 사용자 정보를 가져올 때 호출하며, 데이터베이스에서 사용자 정보를 로드하고 UserDetails 객체를 반환합니다.
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Spring Security가 사용자의 인증 정보를 확인하기 위해 호출하는 메서드
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // DB에서 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // UserDetails 객체로 변환
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole())) // 역할 설정
        );
    }
}