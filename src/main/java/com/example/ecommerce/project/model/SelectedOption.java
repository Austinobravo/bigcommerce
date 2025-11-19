package com.example.ecommerce.project.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class SelectedOption {
    private String name;
    private String value;
}
