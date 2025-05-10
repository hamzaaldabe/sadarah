package com.example.sadarah.Request;

import lombok.Data;

@Data
public class EditProductRequest {
    private String name;
    private String description;
    private double price;
    private String imageUrl;
}
