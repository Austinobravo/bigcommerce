package com.example.ecommerce.project.model;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductOption {

    @Id
    @Column(updatable = false,nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private List<String> values;
}
