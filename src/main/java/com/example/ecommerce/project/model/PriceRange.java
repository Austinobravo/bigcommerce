package com.example.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class PriceRange {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "min_amount")),
            @AttributeOverride(name = "currencyCode", column = @Column(name = "min_currency"))
    })
    private Price minVariantPrice;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "max_amount")),
            @AttributeOverride(name = "currencyCode", column = @Column(name = "max_currency"))
    })
    private Price maxVariantPrice;
}

