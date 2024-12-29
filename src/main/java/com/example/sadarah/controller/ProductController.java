package com.example.sadarah.controller;
import com.example.sadarah.model.Product;
import com.example.sadarah.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(productService.getProducts(page, size));
    }
}
