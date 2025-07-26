package org.soar.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.soar.orderservice.contoller.OrderController;
import org.soar.orderservice.dto.OrderItemRequest;
import org.soar.orderservice.dto.OrderItemResponse;
import org.soar.orderservice.dto.OrderRequest;
import org.soar.orderservice.dto.OrderResponse;
import org.soar.orderservice.model.OrderStatus;
import org.soar.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderRequest orderRequest;
    private OrderResponse orderResponse;

    @BeforeEach
    void setup() {
        OrderItemRequest itemRequest = new OrderItemRequest("prod-123", 2, 10.0);
        orderRequest = new OrderRequest("user-1", "123 Street", List.of(itemRequest));

        OrderItemResponse itemResponse = OrderItemResponse.builder()
                .productId("prod-123")
                .quantity(2)
                .unitPrice(10.0)
                .totalPrice(20.0)
                .build();

        orderResponse = OrderResponse.builder()
                .id(1L)
                .userId("user-1")
                .status(OrderStatus.CREATED)
                .totalAmount(BigDecimal.valueOf(20.0))
                .items(List.of(itemResponse))
                .build();
    }

    @Test
    void testCreateOrder() throws Exception {
        when(orderService.createOrder(any(OrderRequest.class))).thenReturn(orderResponse);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value("user-1"))
                .andExpect(jsonPath("$.totalAmount").value(20.0));
    }

    @Test
    void testGetOrderById() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(orderResponse);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value("user-1"));
    }

    @Test
    void testGetAllOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(orderResponse));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId").value("user-1"));
    }

    @Test
    void testGetOrdersByUserId() throws Exception {
        when(orderService.getOrdersByUserId("user-1")).thenReturn(List.of(orderResponse));

        mockMvc.perform(get("/api/orders/user/user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user-1"));
    }
}
