package com.tagme.tagme_store_back.controller.webModel.request;

public record OrderItemRequest(
        Long productId,
        Long quantity
) {
}
