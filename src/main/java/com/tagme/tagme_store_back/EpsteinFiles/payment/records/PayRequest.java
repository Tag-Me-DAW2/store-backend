package com.tagme.tagme_store_back.EpsteinFiles.payment.records;

import java.math.BigDecimal;

public record PayRequest(
        BigDecimal amount,
        String concept
) {
}
