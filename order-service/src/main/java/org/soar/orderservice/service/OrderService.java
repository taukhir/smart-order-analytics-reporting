package org.soar.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.soar.orderservice.model.Order;
import org.soar.orderservice.model.OrderEvent;
import org.soar.orderservice.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public String placeOrder(Order order) {
        String orderId = UUID.randomUUID().toString();
        order.setOrderId(orderId);
        order.setStatus(OrderStatus.CREATED);

        OrderEvent event = new OrderEvent(orderId, order, "ORDER_CREATED");

        // 🔁 Publish to Kafka
        kafkaTemplate.send("order-events", orderId, event);

        return orderId;
    }

}
