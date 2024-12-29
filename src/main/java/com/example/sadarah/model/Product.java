package com.example.sadarah.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private String imageUrl;

    @ManyToMany(mappedBy = "products")
    private Set<Order> orders;
}
