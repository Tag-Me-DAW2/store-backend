package com.tagme.tagme_store_back.domain.mapper;

import com.tagme.tagme_store_back.domain.dto.OrderItemDto;
import com.tagme.tagme_store_back.domain.model.OrderItem;

public class OrderItemMapper {
    public static OrderItem fromOrderItemDtoToOrderItem(OrderItemDto orderItemDto) {
        if (orderItemDto == null) {
            return null;
        }

        return new OrderItem(
                orderItemDto.id(),
                ProductMapper.fromProductDtoToProduct(orderItemDto.productDto()),
                orderItemDto.quantity(),
                orderItemDto.basePrice(),
                orderItemDto.discountPercentage()
        );
    }

    public static OrderItemDto fromOrderItemToOrderItemDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        return new OrderItemDto(
                orderItem.getId(),
                ProductMapper.fromProductToProductDto(orderItem.getProduct()),
                orderItem.getQuantity(),
                orderItem.getBasePrice(),
                orderItem.getDiscountPercentage(),
                orderItem.getTotal()
        );
    }
}
