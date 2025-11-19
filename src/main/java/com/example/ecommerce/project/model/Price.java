package com.example.ecommerce.project.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Price {
    private String amount;
    private String currencyCode;
}
