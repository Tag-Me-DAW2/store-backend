package com.tagme.tagme_store_back.controller.mapper;

import com.tagme.tagme_store_back.controller.webModel.request.ProductInsertRequest;
import com.tagme.tagme_store_back.controller.webModel.request.ProductUpdateRequest;
import com.tagme.tagme_store_back.controller.webModel.response.CategoryResponse;
import com.tagme.tagme_store_back.controller.webModel.response.ProductDetailResponse;
import com.tagme.tagme_store_back.controller.webModel.response.ProductSummaryResponse;
import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.model.Product;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

public class ProductMapper {
    public static ProductDetailResponse fromProductDtoToProductDetailResponse(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }

        byte[] imageBytes = convertToBytes(productDto.image());

        return new ProductDetailResponse(
                productDto.id(),
                productDto.name(),
                productDto.description(),
                productDto.basePrice(),
                productDto.discountPercentage(),
                productDto.price(),
                imageBytes != null ? Base64.getEncoder().encodeToString(imageBytes) : null,
                CategoryMapper.fromCategoryDtoToCategoryResponse(productDto.category())
        );
    }

    public static ProductSummaryResponse fromProductDtoToProductSummaryResponse(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }

        byte[] imageBytes = convertToBytes(productDto.image());

        return new ProductSummaryResponse(
                productDto.id(),
                productDto.name(),
                productDto.discountPercentage(),
                productDto.price(),
                imageBytes != null ? Base64.getEncoder().encodeToString(imageBytes) : null,
                CategoryMapper.fromCategoryDtoToCategoryResponse(productDto.category())
        );
    }

    public static ProductDto fromProductInsertRequestToProductDto(ProductInsertRequest productInsertRequest) throws SQLException, IOException {
        if (productInsertRequest == null) {
            return null;
        }
        return new ProductDto(
                null,
                productInsertRequest.name(),
                productInsertRequest.description(),
                productInsertRequest.basePrice(),
                productInsertRequest.discountPercentage(),
                null,
                convertToBlob(productInsertRequest.image()),
                new CategoryDto(productInsertRequest.categoryId(), null)
        );
    }

    public static ProductDto fromProductUpdateRequestToProductDto(ProductUpdateRequest productUpdateRequest) throws SQLException, IOException {
        if (productUpdateRequest == null) {
            return null;
        }
        return new ProductDto(
                productUpdateRequest.id(),
                productUpdateRequest.name(),
                productUpdateRequest.description(),
                productUpdateRequest.basePrice(),
                productUpdateRequest.discountPercentage(),
                null,
                convertToBlob(productUpdateRequest.image()),
                new CategoryDto(productUpdateRequest.categoryId(), null)
        );
    }

    private static byte[] convertToBytes(Blob blob) {
        if (blob == null) {
            return null;
        }
        try {
            return blob.getBytes(1, (int) blob.length());
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Blob to byte array", e);
        }
    }

    private static Blob convertToBlob(String base64) throws SQLException, IOException {
        if (base64 == null || base64.isBlank()) {
            return null;
        }
        byte[] bytes = Base64.getDecoder().decode(base64);
        return new SerialBlob(bytes);
    }
}
