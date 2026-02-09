package com.tagme.tagme_store_back.integration.payment.records;

public record AuthRequest(
        String apiKey,
        String username
) {
}
