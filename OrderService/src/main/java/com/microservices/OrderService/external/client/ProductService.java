package com.microservices.OrderService.external.client;

import com.microservices.OrderService.exception.OrderServiceException;
import com.microservices.OrderService.external.response.ProductResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PRODUCT-SERVICE/product")
public interface ProductService {

    @PutMapping("/reduceQuantity/{productId}")
    public ResponseEntity<Void> reduceQuantity(@PathVariable("productId") Long productId,
                                               @RequestParam Long quantity);

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById (@PathVariable Long productId);

    default void fallback(Exception e) {
        throw new OrderServiceException("Payment Service is not available.",
                "UNAVAILABLE",
                500);
    }
}
