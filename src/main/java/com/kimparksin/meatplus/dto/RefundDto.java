package com.kimparksin.meatplus.dto;

import com.kimparksin.meatplus.entity.Refund;
import com.kimparksin.meatplus.entity.RefundStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundDto {
    private Long id;
    private Long paymentId;
    private LocalDateTime refundDate;
    private BigDecimal refundAmount;
    private String refundStatus;
    private String reason;

    public RefundDto(Refund refund) {
        this.id = refund.getId();
        this.paymentId = refund.getPayment().getId();
        this.refundDate = refund.getRefundDate();
        this.refundAmount = refund.getRefundAmount();
        this.refundStatus = refund.getRefundStatus().name();
        this.reason = refund.getReason();
    }

    public Refund toEntity() {
        Refund refund = new Refund();
        refund.setId(this.id);
        refund.setRefundDate(this.refundDate);
        refund.setRefundAmount(this.refundAmount);
        refund.setReason(this.reason);
        refund.setRefundStatus(RefundStatus.valueOf(this.refundStatus));
        return refund;
    }
}
