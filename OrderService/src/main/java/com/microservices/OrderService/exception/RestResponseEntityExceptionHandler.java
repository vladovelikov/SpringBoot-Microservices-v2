package com.microservices.OrderService.exception;

import com.microservices.OrderService.external.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OrderServiceException.class)
    public ResponseEntity<ErrorResponse> handleOrderServiceException(OrderServiceException e) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorMessage(e.getMessage())
                .errorCode(e.getErrorCode())
                .build(),
                HttpStatus.valueOf(e.getStatus()));
    }
}
