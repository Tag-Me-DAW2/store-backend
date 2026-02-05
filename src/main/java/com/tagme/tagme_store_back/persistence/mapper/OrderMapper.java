package com.tagme.tagme_store_back.persistence.mapper;

import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.ShippingInfoJpaEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrderMapper {
    public static OrderJpaEntity toJpaEntity(OrderDto order) {
        if (order == null) {
            return null;
        }
        OrderStatus orderStatus = order.orderStatus();
        OrderJpaEntity entity = new OrderJpaEntity(
                order.id(),
                UserMapper.fromUserDtoToUserJpaEntity(order.user()),
                orderStatus,
                order.orderItems() != null
                        ? order.orderItems().stream()
                                .map(item -> OrderItemMapper.toJpaEntity(item, orderStatus))
                                .toList()
                        : new ArrayList<>(),
                order.shippingCost()
        );
        entity.setPaidDate(order.paidDate());
        entity.setCreatedAt(order.createdAt() != null ? order.createdAt() : LocalDateTime.now());
        
        if (order.shippingInfo() != null) {
            ShippingInfoJpaEntity shippingInfoEntity = ShippingInfoMapper.toJpaEntity(order.shippingInfo(), entity);
            entity.setShippingInfo(shippingInfoEntity);
        }
        
        return entity;
    }

    /**
     * Convierte entidad JPA a OrderDto considerando el estado del pedido:
     * - PENDING: Los items usan datos actuales del producto
     * - PAYED/PROCESSING: Los items usan el snapshot guardado
     */
    public static OrderDto fromJpaEntity(OrderJpaEntity orderJpaEntity) {
        if (orderJpaEntity == null) {
            return null;
        }
        
        OrderStatus orderStatus = orderJpaEntity.getOrderStatus();
        
        return new OrderDto(
                orderJpaEntity.getId(),
                UserMapper.fromUserJpaEntityToUserDto(orderJpaEntity.getUser()),
                orderStatus,
                orderJpaEntity.getOrderItems().stream()
                        .map(item -> OrderItemMapper.fromJpaEntity(item, orderStatus))
                        .toList(),
                null,
                orderJpaEntity.getShippingCost(),
                ShippingInfoMapper.fromJpaEntity(orderJpaEntity.getShippingInfo()),
                orderJpaEntity.getPaidDate(),
                orderJpaEntity.getCreatedAt()
        );
    }
}
