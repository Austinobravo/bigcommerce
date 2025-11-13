package com.example.ecommerce.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product")
@NoArgsConstructor
@Getter
@Setter

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(nullable = false, unique = true)
    private String slug;

    private boolean availableForSale;

    @Column(nullable = false)
    private String title;

    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> options;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private PriceRange priceRange;

    private String featuredImage;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<String> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tags> tags;

    @JsonIgnore()
    private boolean isDeleted;

    @JsonIgnore()
    private LocalDateTime deletedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @JsonIgnore()
    @LastModifiedDate
    private Instant updatedAt;
}
