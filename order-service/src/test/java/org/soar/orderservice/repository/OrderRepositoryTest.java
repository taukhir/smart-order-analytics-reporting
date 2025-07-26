package org.soar.orderservice.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.soar.orderservice.model.Order;
import org.soar.orderservice.model.OrderItem;
import org.soar.orderservice.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Find orders by user ID")
    void testFindByUserId() {
        Order order = Order.builder()
                .userId("user-123")
                .status(OrderStatus.CREATED)
                .shippingAddress("123 Street")
                .build();

        OrderItem item = OrderItem.builder()
                .productId("p-1")
                .quantity(2)
                .unitPrice(100)
                .totalPrice(200)
                .build();
        order.addItem(item);

        orderRepository.save(order);

        List<Order> result = orderRepository.findByUserId("user-123");
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getUserId()).isEqualTo("user-123");
    }
}
