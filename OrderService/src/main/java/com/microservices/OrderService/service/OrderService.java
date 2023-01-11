package com.microservices.OrderService.service;

import com.microservices.OrderService.model.OrderRequest;
import com.microservices.OrderService.model.OrderResponse;

public interface OrderService {
    Long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(Long orderId);
}
