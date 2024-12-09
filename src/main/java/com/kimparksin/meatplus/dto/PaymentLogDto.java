package com.kimparksin.meatplus.dto;

import com.kimparksin.meatplus.entity.PaymentLog;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentLogDto {
    private Long id;
    private Long paymentId;
    private LocalDateTime logDate;
    private String statusMessage;
    private String responseData;

    public PaymentLogDto(PaymentLog paymentLog) {
        this.id = paymentLog.getId();
        this.paymentId = paymentLog.getPayment().getId();
        this.logDate = paymentLog.getLogDate();
        this.statusMessage = paymentLog.getStatusMessage();
        this.responseData = paymentLog.getResponseData();
    }
}
