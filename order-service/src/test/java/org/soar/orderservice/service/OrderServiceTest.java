package org.soar.orderservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soar.orderservice.dto.OrderItemRequest;
import org.soar.orderservice.dto.OrderRequest;
import org.soar.orderservice.dto.OrderResponse;
import org.soar.orderservice.exceptions.OrderNotFoundException;
import org.soar.orderservice.model.Order;
import org.soar.orderservice.model.OrderItem;
import org.soar.orderservice.model.OrderStatus;
import org.soar.orderservice.repository.OrderRepository;
import org.soar.orderservice.service.impl.OrderEventPublisher;
import org.soar.orderservice.service.impl.OrderServiceImpl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventPublisher orderEventPublisher;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order sampleOrder;

    @BeforeEach
    void setup() {
        OrderItem item = OrderItem.builder()
                .productId("P1")
                .quantity(2)
                .unitPrice(50.0)
                .totalPrice(100.0)
                .build();

        sampleOrder = Order.builder()
                .id(1L)
                .userId("user123")
                .status(OrderStatus.CREATED)
                .shippingAddress("Mumbai")
                .totalAmount(BigDecimal.valueOf(100.0))
                .items(List.of(item))
                .build();
    }

    @Test
    void testCreateOrder() {
        OrderRequest request = OrderRequest.builder()
                .userId("user123")
                .shippingAddress("Mumbai")
                .items(List.of(
                        OrderItemRequest.builder()
                                .productId("P1")
                                .quantity(2)
                                .unitPrice(50.0)
                                .build()
                ))
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);

        OrderResponse response = orderService.createOrder(request);

        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo("user123");
        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getTotalAmount()).isEqualTo(BigDecimal.valueOf(100.0));

        verify(orderRepository, times(1)).save(any(Order.class));
        // verify(orderEventPublisher).publishOrderCreatedEvent(any(OrderEvent.class));
    }

    @Test
    void testGetOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));

        OrderResponse response = orderService.getOrderById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo("user123");
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderById(99L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage("Order not found with id: 99");
    }

    @Test
    void testGetOrdersByUserId() {
        when(orderRepository.findByUserId("user123")).thenReturn(List.of(sampleOrder));

        List<OrderResponse> responses = orderService.getOrdersByUserId("user123");

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getUserId()).isEqualTo("user123");
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(sampleOrder));

        List<OrderResponse> responses = orderService.getAllOrders();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getTotalAmount()).isEqualTo(BigDecimal.valueOf(100.0));
    }
}
