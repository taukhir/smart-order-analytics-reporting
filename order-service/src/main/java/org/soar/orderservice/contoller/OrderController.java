package org.soar.orderservice.contoller;

import lombok.RequiredArgsConstructor;
import org.soar.orderservice.model.Order;
import org.soar.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        String orderId = orderService.placeOrder(order);
        return ResponseEntity.ok("Order placed with ID: " + orderId);
    }

}
