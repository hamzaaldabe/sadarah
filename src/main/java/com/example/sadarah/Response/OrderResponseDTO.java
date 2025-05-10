package com.example.sadarah.Response;

import com.example.sadarah.model.OrderStatus;
import com.example.sadarah.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private OrderStatus status;
    private User user;
    private List<ProductResponseDTO> products;
}
