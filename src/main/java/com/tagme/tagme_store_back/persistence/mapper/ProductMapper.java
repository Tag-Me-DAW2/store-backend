package com.tagme.tagme_store_back.persistence.mapper;

import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.ProductJpaEntity;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;

public class ProductMapper {

    public static ProductJpaEntity fromProductDtoToProductJpaEntity(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }

        byte[] imageBytes = null;

        try {
            Blob blob = productDto.image();
            if (blob != null) {
                imageBytes = blob.getBytes(1, (int) blob.length());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error convirtiendo Blob a byte[]", e);
        }

        return new ProductJpaEntity(
                productDto.id(),
                productDto.name(),
                productDto.description(),
                productDto.basePrice(),
                productDto.discountPercentage(),
                imageBytes,
                CategoryMapper.fromCategoryDtoToCategoryJpaEntity(productDto.category())
        );
    }

    public static ProductDto fromProductJpaEntityToProductDto (ProductJpaEntity productJpaEntity) {
        if (productJpaEntity == null) {
            return null;
        }

        Blob blobImage = null;

        try {
            byte[] imageBytes = productJpaEntity.getImage();
            if (imageBytes != null) {
                blobImage = new SerialBlob(imageBytes);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error convirtiendo byte[] a Blob", e);
        }

        return new ProductDto(
                productJpaEntity.getId(),
                productJpaEntity.getName(),
                productJpaEntity.getDescription(),
                productJpaEntity.getBasePrice(),
                productJpaEntity.getDiscountPercentage(),
                null,
                blobImage,
                CategoryMapper.fromCategoryJpaEntityToCategoryDto(productJpaEntity.getCategory())
        );
    }
}
