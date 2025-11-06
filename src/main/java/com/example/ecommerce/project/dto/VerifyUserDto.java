package com.example.ecommerce.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUserDto {
    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Email is required")
    private  String email;

    @Size(min = 6, message = "Code must be at least six characters")
    @NotBlank(message = "Verification code is required")
    private  String verificationCode;

}
