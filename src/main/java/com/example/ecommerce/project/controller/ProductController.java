package com.example.ecommerce.project.controller;

import com.example.ecommerce.project.model.Product;
import com.example.ecommerce.project.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductController {

    private ProductService productService;

    @GetMapping("/products")
    private ResponseEntity<List<Product>> getAllProducts (
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean availableForSale,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxPrice

    ){
        List<Product> allProducts = productService.getAllProducts(title, availableForSale, tag, maxPrice, minPrice);
        return ResponseEntity.of(Optional.ofNullable(allProducts));
    }

}
