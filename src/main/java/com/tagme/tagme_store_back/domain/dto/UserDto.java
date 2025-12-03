package com.tagme.tagme_store_back.domain.dto;

import com.tagme.tagme_store_back.domain.model.UserRole;

public record UserDto(
        Long id,
        String username,
        String password,
        String email,
        String firstName,
        String lastName,
        String phone,
        UserRole role
) {
}
