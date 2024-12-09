package com.kimparksin.meatplus.controller;

import com.kimparksin.meatplus.dto.RefundDto;
import com.kimparksin.meatplus.service.RefundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/refunds")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    //클라이언트가 환불 데이터를 JSON 형식으로 전송하면 새로운 환불 요청을 생성
    @PostMapping
    public ResponseEntity<RefundDto> createRefund(@RequestBody RefundDto refundDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(refundService.createRefund(refundDto));

    }

    //모든 환불 데이터를 JSON 배열 형식으로 반환.
    @GetMapping
    public ResponseEntity<List<RefundDto>> getAllRefunds() {
        return ResponseEntity.ok(refundService.getAllRefunds());
    }

    //경로 변수 id를 사용하여 특정 환불 요청을 조회.
    @GetMapping("/{id}")
    public ResponseEntity<RefundDto> getRefundById(@PathVariable Long id) {
        return ResponseEntity.ok(refundService.getRefundById(id));
    }
}
