package com.tagme.tagme_store_back.controller.webModel.request;

import java.math.BigDecimal;

public record OrderInfoRequest(
        BigDecimal shippingCost,
        BigDecimal subtotal,
        BigDecimal total
) {
}
