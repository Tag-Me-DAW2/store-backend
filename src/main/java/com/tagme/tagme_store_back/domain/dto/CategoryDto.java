package com.tagme.tagme_store_back.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CategoryDto(
        Long id,
        @NotBlank(message = "Name cannot be empty or null")
        String name
) {
}
