package com.microservices.OrderService.service.impl;

import com.microservices.OrderService.entity.Order;
import com.microservices.OrderService.exception.OrderServiceException;
import com.microservices.OrderService.external.client.PaymentService;
import com.microservices.OrderService.external.client.ProductService;
import com.microservices.OrderService.external.request.PaymentRequest;
import com.microservices.OrderService.external.response.PaymentResponse;
import com.microservices.OrderService.external.response.ProductResponse;
import com.microservices.OrderService.model.OrderRequest;
import com.microservices.OrderService.model.OrderResponse;
import com.microservices.OrderService.repository.OrderRepository;
import com.microservices.OrderService.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final PaymentService paymentService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.paymentService = paymentService;
    }

    @Override
    public Long placeOrder(OrderRequest orderRequest) {

        //Call the ProductService and block the products (reduce their quantity)
        //Call the PaymentService to complete the payment -> Payment Success -> Completed -> Else -> Canceled
        //OrderService - create the order and save the data

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        Order order = Order.builder()
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .amount(orderRequest.getAmount())
                .orderDate(Instant.now())
                .orderStatus("CREATED")
                .build();

        orderRepository.save(order);

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .amount(order.getAmount())
                .paymentMode(orderRequest.getPaymentMode())
                .build();

        String orderStatus = null;

        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done successfully. Changing the order status to PLACED");
            orderStatus = "PLACED";
        } catch (Exception e) {
            log.info("Error occurred in payment. Changing the order status to PAYMENT_FAILED.");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        log.info("Order placed successfully with order id {}", order.getId());

        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(Long orderId) {
        log.info("Getting information for order with Order Id {}.", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderServiceException("Order not found with order id: " + orderId,
                        "NOT_FOUND",
                        404));

        log.info("Getting information for product with Product id {}.", order.getProductId());

        ResponseEntity<ProductResponse> productResponse = productService.getProductById(order.getProductId());
        ProductResponse product = new ProductResponse();

        if(productResponse.getStatusCode().is2xxSuccessful()) {
            product = productResponse.getBody();
        } else {
            log.info("Unable to find product with Product id {}.", order.getProductId());
            throw new OrderServiceException(
                    String.format("Product with id %s not found in order with id %s.", order.getProductId(), orderId),
                    "NOT_FOUND",
                    404);
        }

        ResponseEntity<PaymentResponse> paymentResponse = paymentService.getPaymentDetailsByOrderId(orderId);
        PaymentResponse payment = new PaymentResponse();

        if(paymentResponse.getStatusCode().is2xxSuccessful()) {
            payment = paymentResponse.getBody();
        } else {
            log.info("Unable to find payment with payment for order with Order id {}.", orderId);
            throw new OrderServiceException(
                    String.format("Payment not found for order with Order id %s.", orderId),
                    "NOT_FOUND",
                    404
            );
        }

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .product(product)
                .payment(payment)
                .build();

        return orderResponse;
    }
}
