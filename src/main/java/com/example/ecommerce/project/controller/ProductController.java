package com.example.ecommerce.project.controller;

import com.example.ecommerce.project.dto.CreateProductDto;
import com.example.ecommerce.project.dto.UpdateProductDto;
import com.example.ecommerce.project.model.Product;
import com.example.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @PostMapping(value = "/product/create", consumes = {"multipart/form-data"})
    private ResponseEntity<?> createProduct(@Valid @ModelAttribute CreateProductDto input) throws IOException {

        try{
            Product product = productService.createProduct(input);
            return ResponseEntity.ok(product);

        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @PatchMapping(value = "/product/update/${productId}", consumes = {"multipart/form-data"})
    private ResponseEntity<?> updateProduct(@PathVariable String productId, @ModelAttribute UpdateProductDto input){
        try{
            Product product = productService.updateProduct(productId, input);
            return ResponseEntity.ok(product);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

}
