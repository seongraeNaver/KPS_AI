package com.kimparksin.meatplus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "image")
@Getter
@Setter
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "input_img", nullable = false, length = 255)
    private String inputImg;

    @Column(name = "output_img", length = 255)
    private String outputImg;

    @Column(name = "input_time", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime inputTime = LocalDateTime.now();

    @Column(name = "output_time")
    private LocalDateTime outputTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_code", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자와의 관계
}