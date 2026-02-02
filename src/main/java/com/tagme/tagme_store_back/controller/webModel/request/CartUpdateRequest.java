package com.tagme.tagme_store_back.controller.webModel.request;

import java.util.List;

public record CartUpdateRequest(
        Long userId,
        List<CartItemRequest> orderItems
) {
}

