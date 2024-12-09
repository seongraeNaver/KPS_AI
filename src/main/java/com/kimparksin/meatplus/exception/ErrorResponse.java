package com.kimparksin.meatplus.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor

//예외 발생 시 클라이언트에게 반환될 응답 데이터 구조를 정의한 클래스.
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}
