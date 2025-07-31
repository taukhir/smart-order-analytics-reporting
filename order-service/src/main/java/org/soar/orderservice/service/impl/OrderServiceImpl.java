package org.soar.orderservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.soar.orderservice.dto.OrderItemRequest;
import org.soar.orderservice.dto.OrderItemResponse;
import org.soar.orderservice.dto.OrderRequest;
import org.soar.orderservice.dto.OrderResponse;
import org.soar.orderservice.exceptions.OrderNotFoundException;
import org.soar.orderservice.model.Order;
import org.soar.orderservice.model.OrderEvent;
import org.soar.orderservice.model.OrderItem;
import org.soar.orderservice.model.OrderStatus;
import org.soar.orderservice.repository.OrderRepository;
import org.soar.orderservice.service.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisherImpl orderEventPublisherImpl;


    @Override
    public OrderResponse createOrder(OrderRequest request) {
        Order order = Order.builder()
                .userId(request.getUserId())
                .status(OrderStatus.CREATED)
                .shippingAddress(request.getShippingAddress())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.getItems()) {
            double itemTotal = itemReq.getUnitPrice() * itemReq.getQuantity();
            totalAmount = totalAmount.add(BigDecimal.valueOf(itemTotal));

            OrderItem item = OrderItem.builder()
                    .productId(itemReq.getProductId())
                    .quantity(itemReq.getQuantity())
                    .unitPrice(itemReq.getUnitPrice())
                    .totalPrice(itemTotal)
                    .build();

            order.addItem(item);
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        OrderEvent event = new OrderEvent(order.getId(), order, order.getStatus().name());
        orderEventPublisherImpl.publishOrderCreatedEvent(event);
        return mapToResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(i -> OrderItemResponse.builder()
                        .productId(i.getProductId())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .totalPrice(i.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .items(itemResponses)
                .build();
    }

}
