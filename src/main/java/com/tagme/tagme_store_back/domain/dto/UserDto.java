package com.tagme.tagme_store_back.domain.dto;

import com.tagme.tagme_store_back.domain.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserDto(
        Long id,
        @NotBlank(message = "Username cannot be empty or null")
        String username,
        @NotBlank(message = "Password cannot be empty or null")
        String password,
        @NotBlank(message = "Email cannot be empty or null")
        @Email(message = "Email must be a valid email address")
        String email,
        @NotBlank(message = "First name cannot be empty or null")
        String firstName,
        String lastName,
        @Pattern(regexp = "^\\+?[1-9]\\d{6,14}$", message = "Phone number must be a valid international phone number")
        String phone,
        @NotNull(message = "Role cannot be null")
        UserRole role
) {
}
