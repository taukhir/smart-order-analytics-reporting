package org.soar.orderservice.service;

import org.soar.orderservice.dto.OrderRequest;
import org.soar.orderservice.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getOrdersByUserId(String userId);
}
