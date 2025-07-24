package org.soar.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private Long orderId;
    private Order order;
    private String eventType; // e.g., ORDER_CREATED, ORDER_CONFIRMED, etc.
}