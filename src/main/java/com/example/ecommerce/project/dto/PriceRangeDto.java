package com.example.ecommerce.project.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PriceRangeDto {
    private PriceDto maxVariantPrice;
    private PriceDto minVariantPrice;
}

