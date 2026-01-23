package com.tagme.tagme_store_back.persistence.mapper;

import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderJpaEntity;

public class OrderMapper {
    public static OrderJpaEntity toJpaEntity(OrderDto order) {
        if (order == null) {
            return null;
        }
        return new OrderJpaEntity(
                order.id(),
                UserMapper.fromUserDtoToUserJpaEntity(order.user()),
                order.orderStatus(),
                order.orderItems().stream()
                        .map(OrderItemMapper::toJpaEntity)
                        .toList()
        );
    }

    public static OrderDto fromJpaEntity(OrderJpaEntity orderJpaEntity) {
        if (orderJpaEntity == null) {
            return null;
        }
        return new OrderDto(
                orderJpaEntity.getId(),
                UserMapper.fromUserJpaEntityToUserDto(orderJpaEntity.getUser()),
                orderJpaEntity.getOrderStatus(),
                orderJpaEntity.getOrderItems().stream()
                        .map(OrderItemMapper::fromJpaEntity)
                        .toList(),
                null,
                orderJpaEntity.getCreatedAt()
        );
    }
}
