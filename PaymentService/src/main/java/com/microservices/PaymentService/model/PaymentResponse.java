package com.microservices.PaymentService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private long paymentId;
    private String status;
    private BigDecimal amount;
    private PaymentMode paymentMode;
    private Instant paymentDate;
    private long orderId;

}
