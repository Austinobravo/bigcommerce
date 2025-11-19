package com.example.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "product_variant")
@Getter
@Setter
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private boolean availableForSale;

    private String title;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "variant_amount")),
            @AttributeOverride(name = "currencyCode", column = @Column(name = "variant_currency"))
    })
    private Price price;

    @ElementCollection
    @CollectionTable(name = "variant_options", joinColumns = @JoinColumn(name = "variant_id"))
    private List<SelectedOption> options;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
