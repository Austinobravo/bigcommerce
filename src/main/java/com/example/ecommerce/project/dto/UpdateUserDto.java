package com.example.ecommerce.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserDto {

    @Size(min = 6, message = "Password must be at least six characters")
    @NotBlank(message = "Password is required")
    private String password;
}
