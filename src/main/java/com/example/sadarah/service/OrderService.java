package com.example.sadarah.service;

import com.example.sadarah.Request.OrderRequest;
import com.example.sadarah.model.*;
import com.example.sadarah.repository.OrderProductRepository;
import com.example.sadarah.repository.OrderRepository;
import com.example.sadarah.repository.ProductRepository;
import com.example.sadarah.repository.UserRepository;
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

    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private UserRepository userRepository;

    public Order placeOrder(User user, OrderRequest orderRequest) {
        User user1 = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Order order = Order.builder()
                .user(user1)
                .status(OrderStatus.PENDING)
                .build();
        order = orderRepository.save(order);
        orderRepository.flush();
        for (OrderRequest.OrderProductRequest orderProductRequest : orderRequest.getProducts()) {
            Product product = productRepository.findById(orderProductRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + orderProductRequest.getProductId()));

            OrderProduct orderProduct = OrderProduct.builder()
                    .order(order)
                    .product(product)
                    .quantity(orderProductRequest.getQuantity())
                    .build();

            orderProductRepository.save(orderProduct);
        }

        return order;
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
