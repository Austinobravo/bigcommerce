package com.example.ecommerce.project.repository;

import com.example.ecommerce.project.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, String> {
    Optional<Product> findBySlug(String slug);
}
