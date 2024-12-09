package com.kimparksin.meatplus.service;

import com.kimparksin.meatplus.dto.RefundDto;
import com.kimparksin.meatplus.entity.Refund;
import com.kimparksin.meatplus.repository.RefundRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RefundService {

    private final RefundRepository refundRepository;

    public RefundService(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }

    //클라이언트로부터 전달받은 환불 데이터를 데이터베이스에 저장
    public RefundDto createRefund(RefundDto refundDto) {
        log.info("Creating refund for payment ID: {}", refundDto.getPaymentId());
        Refund refund = refundDto.toEntity();
        Refund savedRefund = refundRepository.save(refund);
        return new RefundDto(savedRefund);
    }

    //데이터베이스에서 모든 환불 데이터를 조회
    public List<RefundDto> getAllRefunds() {
        log.info("Fetching all refunds");
        return refundRepository.findAll()
                .stream()
                .map(RefundDto::new)
                .collect(Collectors.toList());
    }

    //특정 환불 ID를 기반으로 환불 데이터를 조회
    public RefundDto getRefundById(Long id) {
        log.info("Fetching refund with id: {}", id);
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refund not found with id: " + id));
        return new RefundDto(refund);
    }
}
