package com.example.sadarah.controller;

import com.example.sadarah.Request.OrderRequest;
import com.example.sadarah.model.Order;
import com.example.sadarah.model.OrderStatus;
import com.example.sadarah.model.Product;
import com.example.sadarah.model.User;
import com.example.sadarah.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(
            @AuthenticationPrincipal User user,
            @RequestBody OrderRequest orderRequest) {
        Order order = orderService.placeOrder(user, orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/status/{orderId}")
    public ResponseEntity<?> getOrderStatus(@PathVariable Long orderId) {
        OrderStatus status = orderService.getOrderStatus(orderId);
        return ResponseEntity.ok(status);
    }

    @GetMapping
    public ResponseEntity<Page<Order>> getOrdersByUserId(@AuthenticationPrincipal User user, Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(user.getId(), pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Order>> getAllOrders(Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {
        if (!userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.status(403).build();
        }

        Page<Order> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Order> changeOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (!userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).build();
        }

        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }
}
