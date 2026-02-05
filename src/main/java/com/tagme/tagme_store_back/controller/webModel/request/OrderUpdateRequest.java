package com.tagme.tagme_store_back.controller.webModel.request;

import java.util.List;

public record OrderUpdateRequest(
        Long userId,
        List<OrderItemRequest> orderItems
) {
}
