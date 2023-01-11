package com.microservices.OrderService.external.request;

import com.microservices.OrderService.model.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    private long orderId;
    private String referenceNumber;
    private BigDecimal amount;
    private PaymentMode paymentMode;

}
