package com.tagme.tagme_store_back.controller.webModel.request;

public record UserUpdateRequest(
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
