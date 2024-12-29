package com.example.sadarah.repository;

import com.example.sadarah.model.Order;
import com.example.sadarah.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Page<Order> findByUserId(Long userId, Pageable pageable);
}
