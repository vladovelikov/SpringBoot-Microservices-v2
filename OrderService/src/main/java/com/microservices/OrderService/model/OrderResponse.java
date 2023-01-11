package com.microservices.OrderService.model;

import com.microservices.OrderService.external.response.PaymentResponse;
import com.microservices.OrderService.external.response.ProductResponse;
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
public class OrderResponse {

    private long orderId;
    private Instant orderDate;
    private BigDecimal amount;
    private String orderStatus;
    private ProductResponse product;
    private PaymentResponse payment;
}
