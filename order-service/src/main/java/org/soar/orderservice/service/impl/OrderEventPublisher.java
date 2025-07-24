package org.soar.orderservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soar.orderservice.model.OrderEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Value("${app.kafka.topics.order-topic}")
    private String orderTopic;

    public void publishOrderCreatedEvent(OrderEvent event) {
        log.info("Publishing order event to Kafka: {}", event);
        kafkaTemplate.send(orderTopic, String.valueOf(event.getOrderId()), event);
    }

    //    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

//    public String placeOrder(Order order) {
//        String orderId = UUID.randomUUID().toString();
//        order.setOrderId(orderId);
//        order.setStatus(OrderStatus.CREATED);
//
//        OrderEvent event = new OrderEvent(orderId, order, "ORDER_CREATED");
//
//        // 🔁 Publish to Kafka
//        kafkaTemplate.send("order-events", orderId, event);
//
//        return orderId;
//    }
}
