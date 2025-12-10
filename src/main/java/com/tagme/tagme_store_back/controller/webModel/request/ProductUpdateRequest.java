package com.tagme.tagme_store_back.controller.webModel.request;

import com.tagme.tagme_store_back.controller.webModel.response.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Base64;

public record ProductUpdateRequest(
        Long id,
        String name,
        String description,
        BigDecimal basePrice,
        BigDecimal discountPercentage,
        String image,
        Long categoryId
) {
}
