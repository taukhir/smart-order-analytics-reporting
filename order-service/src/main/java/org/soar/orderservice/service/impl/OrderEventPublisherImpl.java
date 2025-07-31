package org.soar.orderservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soar.orderservice.model.OrderEvent;
import org.soar.orderservice.service.OrderEventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisherImpl implements OrderEventPublisher {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Value("${app.kafka.topics.order-topic}")
    private String orderTopic;

    @Override
    public void publishOrderCreatedEvent(OrderEvent event) {
        log.info("Publishing order event to Kafka: {}", event);
        kafkaTemplate.send(orderTopic, String.valueOf(event.getOrderId()), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Message failed to send", ex);
                    } else {
                        log.info("Message sent successfully: {}", result);
                    }
                });

        log.info("Published event: {}", event);

    }

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
