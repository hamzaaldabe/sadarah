package com.example.sadarah.service;

import com.example.sadarah.Request.OrderRequest;
import com.example.sadarah.Response.OrderResponseDTO;
import com.example.sadarah.Response.ProductResponseDTO;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findAll(pageable);
        return getOrderResponseDTOS(ordersPage);
    }

    private Page<OrderResponseDTO> getOrderResponseDTOS(Page<Order> ordersPage) {
        return ordersPage.map(order -> {
            List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(order.getId());

            List<ProductResponseDTO> products = orderProducts.stream()
                    .map(orderProduct -> {
                        Product product = orderProduct.getProduct();
                        return new ProductResponseDTO(
                                product.getId(),
                                product.getName(),
                                product.getDescription(),
                                product.getPrice(),
                                orderProduct.getQuantity()
                        );
                    })
                    .toList();
            User user = order.getUser();
            return new OrderResponseDTO(order.getId(), order.getStatus(), order.getUser(), products);
        });
    }


    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Page<OrderResponseDTO> getOrdersWithProducts(Long userId, Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findByUserId(userId, pageable);
        return getOrderResponseDTOS(ordersPage);
    }

}
