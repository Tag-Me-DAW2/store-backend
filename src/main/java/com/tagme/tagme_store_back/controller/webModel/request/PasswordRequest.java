package com.tagme.tagme_store_back.controller.webModel.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PasswordRequest(
        @NotNull(message = "Current password must not be null")
        String currentPassword,
        @NotBlank(message = "New password must not be blank")
        String newPassword,
        @NotBlank(message = "New password confirmation must not be blank")
        String newPasswordConfirmation
) {
}
