package com.tagme.tagme_store_back.domain.dto;

import com.tagme.tagme_store_back.domain.model.UserRole;
import com.tagme.tagme_store_back.domain.validation.ValidEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.sql.Blob;

public record UserDto(
        Long id,
        @NotBlank(message = "Username cannot be empty or null")
        String username,
        String password,
        @ValidEmail
        String email,
        @NotBlank(message = "First name cannot be empty or null")
        String firstName,
        String lastName,
        @Pattern(regexp = "^\\+?[1-9]\\d{6,14}$", message = "Phone number must be a valid international phone number")
        String phone,
        @NotNull(message = "Image cannot be null")
        Blob profilePicture,
        @NotNull(message = "Role cannot be null")
        UserRole role
) {
}
