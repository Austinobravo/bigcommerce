package com.example.ecommerce.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginDto {

    @NotBlank(message = "Id Token is required")
    private String idToken;
}
