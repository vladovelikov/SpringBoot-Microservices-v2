package com.microservices.OrderService.exception;

import lombok.Data;

@Data
public class OrderServiceException extends RuntimeException {

    private String errorCode;
    private Integer status;

    public OrderServiceException(String message, String errorCode, Integer status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
