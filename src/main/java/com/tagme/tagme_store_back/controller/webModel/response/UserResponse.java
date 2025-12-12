package com.tagme.tagme_store_back.controller.webModel.response;

public record UserResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        String phone,
        String profilePicture,
        String role
) {
}
