package com.tagme.tagme_store_back.controller.webModel.request;

public record PasswordRequest(
        String newPassword,
        String newPasswordConfirmation
) {
}
