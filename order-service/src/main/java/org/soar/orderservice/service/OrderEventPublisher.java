package org.soar.orderservice.service;

import org.soar.orderservice.model.OrderEvent;

public interface OrderEventPublisher {

    void publishOrderCreatedEvent(OrderEvent event);
}
