package com.kimparksin.meatplus.repository;

import com.kimparksin.meatplus.entity.Image;
import com.kimparksin.meatplus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    // 특정 프로젝트에 속한 이미지 검색
    List<Image> findByProject_ProjectCode(Long projectCode);

    // 사용자 ID 기반으로 이미지 조회
    List<Image> findByUserId(Long userId);

    // 특정 사용자와 관련된 모든 이미지를 반환하는 메서드
    List<Image> findByUser(User user);
}
