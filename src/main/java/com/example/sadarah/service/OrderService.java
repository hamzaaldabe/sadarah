package com.example.sadarah.service;

import com.example.sadarah.model.Order;
import com.example.sadarah.model.OrderStatus;
import com.example.sadarah.model.Product;
import com.example.sadarah.model.User;
import com.example.sadarah.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order placeOrder(User user, Set<Product> products) {
        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .products(products)
                .build();
        return orderRepository.save(order);
    }

    public OrderStatus getOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        return order.getStatus();
    }

    public Page<Order> getOrdersByUserId(Long userId, int page, int size) {
        return orderRepository.findByUserId(userId, PageRequest.of(page, size));
    }
}
