package com.tagme.tagme_store_back.controller.webModel.request;

import jakarta.validation.constraints.NotBlank;

public record VerifyPasswordRequest(
        @NotBlank(message = "Password must not be blank")
        String password
) {
}
