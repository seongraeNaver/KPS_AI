package com.kimparksin.meatplus.repository;

import com.kimparksin.meatplus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // 이메일로 사용자 찾기
}
