package com.tagme.tagme_store_back.controller.webModel.request;

public record UserInsertRequest(
        String username,
        String password,
        String email,
        String firstName,
        String lastName,
        String phone,
        String profilePicture,
        String role
) {
}
