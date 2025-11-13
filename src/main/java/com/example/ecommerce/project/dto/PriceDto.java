package com.example.ecommerce.project.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PriceDto {
    private String amount;
    private String currencyCode;
}
