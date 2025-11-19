package com.example.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "product_option")
@Getter
@Setter
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @ElementCollection
    @CollectionTable(
            name = "product_option_values",
            joinColumns = @JoinColumn(name = "option_id")
    )
    @Column(name = "value")
    private List<String> values;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}

