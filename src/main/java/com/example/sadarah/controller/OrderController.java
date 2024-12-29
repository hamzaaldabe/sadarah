package com.example.sadarah.controller;

import com.example.sadarah.model.Order;
import com.example.sadarah.model.OrderStatus;
import com.example.sadarah.model.Product;
import com.example.sadarah.model.User;
import com.example.sadarah.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestParam Long userId, @RequestBody Set<Product> products) {
        Order order = orderService.placeOrder(User.builder().id(userId).build(), products);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getOrderStatus(@RequestParam Long orderId) {
        OrderStatus status = orderService.getOrderStatus(orderId);
        return ResponseEntity.ok(status);
    }

    @GetMapping
    public ResponseEntity<Page<Order>> getOrdersByUserId(@RequestParam Long userId, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId, page, size));
    }
}
