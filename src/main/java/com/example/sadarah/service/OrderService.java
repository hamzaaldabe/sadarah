package com.example.sadarah.service;

import com.example.sadarah.model.Order;
import com.example.sadarah.model.OrderStatus;
import com.example.sadarah.model.Product;
import com.example.sadarah.model.User;
import com.example.sadarah.repository.OrderRepository;
import com.example.sadarah.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order placeOrder(User user, List<Long> productIds) {
        Set<Product> products = new HashSet<>(productRepository.findAllById(productIds));

        if (products.isEmpty()) {
            throw new IllegalArgumentException("No valid products found for the given IDs");
        }

        Order order = Order.builder()
                .user(user)
                .products(products)
                .status(OrderStatus.PENDING)
                .build();

        return orderRepository.save(order);
    }


    public OrderStatus getOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        return order.getStatus();
    }

    public Page<Order> getOrdersByUserId(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
