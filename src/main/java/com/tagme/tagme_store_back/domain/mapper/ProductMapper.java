package com.tagme.tagme_store_back.domain.mapper;

import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.model.Product;

public class ProductMapper {
    private static ProductMapper INSTANCE;

    private ProductMapper() {
    }

    public static ProductMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProductMapper();
        }
        return INSTANCE;
    }

    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getBasePrice(),
                product.getDiscountPercentage(),
                product.getImage(),
                CategoryMapper.getInstance().toDto(product.getCategory())
        );
    }

    public Product toModel(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }
        return new Product(
                productDto.id(),
                productDto.name(),
                productDto.description(),
                productDto.basePrice(),
                productDto.discountPercentage(),
                productDto.image(),
                CategoryMapper.getInstance().toModel(productDto.category())
        );
    }
}
