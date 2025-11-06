package com.example.ecommerce.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserPasswordDto {

    @NotBlank(message = "Current Password can not be empty")
    @Size(min = 6, message = "Password must be more than six characters")
    private String currentPassword;

    @NotBlank(message = "New Password can not be empty")
    @Size(min = 6, message = "Password must be more than six characters")
    private String newPassword;
}
