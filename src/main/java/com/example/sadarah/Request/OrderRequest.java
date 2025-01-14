package com.example.sadarah.Request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<OrderProductRequest> products;

    @Data
    public static class OrderProductRequest {
        private Long productId;
        private int quantity;
    }
}

