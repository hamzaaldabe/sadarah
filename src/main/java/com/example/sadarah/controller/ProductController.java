package com.example.sadarah.controller;
import com.example.sadarah.Request.EditProductRequest;
import com.example.sadarah.model.Product;
import com.example.sadarah.model.User;
import com.example.sadarah.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getProducts(pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Long productId) {
        try {
            return ResponseEntity.ok(productService.getProduct(productId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId, @RequestBody EditProductRequest editProductRequest) {
        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (!roles.contains("ADMIN")) {
            return ResponseEntity.status(403).build();
        }
        try {
            return ResponseEntity.ok(productService.updateProduct(productId, editProductRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product, @AuthenticationPrincipal UserDetails userDetails) {
        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (!roles.contains("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        Product savedProduct = productService.createProduct(product);
        return ResponseEntity.ok(savedProduct);
    }
}
