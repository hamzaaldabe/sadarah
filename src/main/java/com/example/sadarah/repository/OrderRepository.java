package com.example.sadarah.repository;

import com.example.sadarah.model.Order;
import com.example.sadarah.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Page<Order> findByUserId(Long userId, Pageable pageable);
    @Query(value = """
    SELECT o.*
    FROM sadarah.orders o
    LEFT JOIN sadarah.order_products op ON o.id = op.order_id
    LEFT JOIN sadarah.products p ON op.product_id = p.id
    WHERE o.user_id = :userId
""",
            countQuery = """
    SELECT COUNT(o.id)
    FROM sadarah.orders o
    WHERE o.user_id = :userId
""",
            nativeQuery = true)
    Page<Order> findByUserIdWithProductsNative(@Param("userId") Long userId, Pageable pageable);



}
