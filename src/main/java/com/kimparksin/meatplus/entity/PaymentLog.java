package com.kimparksin.meatplus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_log")
@Getter
@Setter
@NoArgsConstructor
public class PaymentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false) // 명시적 매핑
    private Payment payment;

    @Column(name = "log_date", nullable = false) // 명시적 매핑
    private LocalDateTime logDate = LocalDateTime.now();

    @Column(name = "status_message") // 명시적 매핑
    private String statusMessage;

    @Column(name = "response_data") // 명시적 매핑
    private String responseData;
}

