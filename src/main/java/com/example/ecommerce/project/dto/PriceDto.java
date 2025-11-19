package com.example.ecommerce.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PriceDto {
    @NotBlank(message = "Amount is required")
    private String amount;
    @NotBlank(message = "Currency is required")
    private String currencyCode;
}
