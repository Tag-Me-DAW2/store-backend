package com.tagme.tagme_store_back.persistence.mapper;

import com.tagme.tagme_store_back.domain.dto.OrderItemDto;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderItemJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.ProductJpaEntity;

public class OrderItemMapper {

    public static OrderItemJpaEntity toJpaEntity(OrderItemDto orderItemDto) {
        if (orderItemDto == null) {
            return null;
        }

        ProductJpaEntity productEntity = ProductMapper.fromProductDtoToProductJpaEntity(orderItemDto.productDto());

        return new OrderItemJpaEntity(
                orderItemDto.id(),
                null, // order se setea después
                productEntity,
                productEntity != null ? productEntity.getName() : null,
                productEntity != null ? productEntity.getImage() : null,
                productEntity != null ? productEntity.getImageName() : null,
                orderItemDto.basePrice() != null ? orderItemDto.basePrice() :
                        (productEntity != null ? productEntity.getBasePrice() : null),
                orderItemDto.discountPercentage() != null ? orderItemDto.discountPercentage() :
                        (productEntity != null ? productEntity.getDiscountPercentage() : null),
                orderItemDto.quantity()
        );
    }

    public static OrderItemDto fromJpaEntity(OrderItemJpaEntity orderItemJpaEntity) {
        if (orderItemJpaEntity == null) {
            return null;
        }

        return new OrderItemDto(
                orderItemJpaEntity.getId(),
                ProductMapper.fromProductJpaEntityToProductDto(orderItemJpaEntity.getProduct()),
                orderItemJpaEntity.getQuantity(),
                orderItemJpaEntity.getBasePrice(),
                orderItemJpaEntity.getDiscountPercentage(),
                null // total se calcula en el dominio
        );
    }
}
