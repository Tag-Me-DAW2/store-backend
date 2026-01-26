package com.tagme.tagme_store_back.persistence.mapper;

import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.OrderItemDto;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderItemJpaEntity;

public class OrderItemMapper {
    public static OrderItemJpaEntity toJpaEntity(OrderItemDto orderItemDto) {
        if (orderItemDto == null) {
            return null;
        }
        return new OrderItemJpaEntity(
                orderItemDto.id(),
                null,
                ProductMapper.fromProductDtoToProductJpaEntity(orderItemDto.productDto()),
                orderItemDto.quantity(),
                orderItemDto.basePrice(),
                orderItemDto.discountPercentage()
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
                null
        );
    }
}
