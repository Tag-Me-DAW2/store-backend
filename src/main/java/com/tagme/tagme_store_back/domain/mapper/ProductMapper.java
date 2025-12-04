package com.tagme.tagme_store_back.domain.mapper;

import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.model.Product;

public class ProductMapper {

    public static ProductDto fromProductToProductDto (Product product) {
        if (product == null) {
            return null;
        }
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getBasePrice(),
                product.getDiscountPercentage(),
                product.getPrice(),
                product.getImage(),
                CategoryMapper.fromCategoryToCategoryDto(product.getCategory())
        );
    }

    public static Product fromProductDtoToProduct (ProductDto productDto) {
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
                CategoryMapper.fromCategoryDtoToCategory(productDto.category())
        );
    }
}
