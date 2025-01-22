package com.example.sadarah.service;

import com.example.sadarah.Request.EditProductRequest;
import com.example.sadarah.model.Product;
import com.example.sadarah.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getProduct(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            return productOptional.get();
        }
        else throw new RuntimeException();
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, EditProductRequest editProductRequest) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(editProductRequest.getName());
            product.setDescription(editProductRequest.getDescription());
            product.setPrice(editProductRequest.getPrice());
            product.setImageUrl(editProductRequest.getImageUrl());
            product = productRepository.save(product);
            return product;
        }
        else throw new RuntimeException();
    }
}
