package com.example.ecommerce.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PriceRangeDto {
    @NotBlank(message = "Max Price is required")
    private PriceDto maxVariantPrice;
    @NotBlank(message = "Min Price is required")
    private PriceDto minVariantPrice;
}

