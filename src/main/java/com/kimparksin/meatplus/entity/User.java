package com.kimparksin.meatplus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "pwd", nullable = false, length = 255)
    private String pwd;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "reg_date", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime regDate = LocalDateTime.now();

    // 추가된 필드
    @Column(name = "role", nullable = false, length = 20)
    private String role = "USER"; // 기본 사용자 역할

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true; // 계정 활성화 여부

    // Spring Security에서 요구하는 메서드 이름으로 맞춰줍니다.
    public String getPassword() {
        return this.pwd;
    }
}
