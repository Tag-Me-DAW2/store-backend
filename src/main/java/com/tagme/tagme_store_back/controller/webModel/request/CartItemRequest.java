package com.tagme.tagme_store_back.controller.webModel.request;

import java.math.BigDecimal;

public record CartItemRequest(
        Long productId,
        Long quantity
) {
}

