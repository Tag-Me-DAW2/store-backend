package com.tagme.tagme_store_back.persistence.mapper;

import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.OrderItemDto;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.ShippingInfoJpaEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        
        // Calculate totalPrice only for orders (not carts)
        if (orderStatus == OrderStatus.PAYED || orderStatus == OrderStatus.PROCESSING) {
            BigDecimal totalPrice = calculateTotalPrice(order);
            entity.setTotalPrice(totalPrice);
        }
        
        if (order.shippingInfo() != null) {
            ShippingInfoJpaEntity shippingInfoEntity = ShippingInfoMapper.toJpaEntity(order.shippingInfo(), entity);
            entity.setShippingInfo(shippingInfoEntity);
        }
        
        return entity;
    }
    
    private static BigDecimal calculateTotalPrice(OrderDto order) {
        if (order.orderItems() == null || order.orderItems().isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        return order.orderItems().stream()
                .map(OrderMapper::calculateItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private static BigDecimal calculateItemTotal(OrderItemDto item) {
        if (item.basePrice() == null || item.quantity() == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal basePrice = item.basePrice();
        BigDecimal quantity = BigDecimal.valueOf(item.quantity());
        BigDecimal discountPercentage = item.discountPercentage() != null ? item.discountPercentage() : BigDecimal.ZERO;
        
        // Calculate: basePrice * quantity * (1 - discountPercentage / 100)
        BigDecimal discountMultiplier = BigDecimal.ONE.subtract(
                discountPercentage.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
        );
        
        return basePrice.multiply(quantity).multiply(discountMultiplier).setScale(2, RoundingMode.HALF_UP);
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
                orderJpaEntity.getTotalPrice(),
                orderJpaEntity.getShippingCost(),
                ShippingInfoMapper.fromJpaEntity(orderJpaEntity.getShippingInfo()),
                orderJpaEntity.getPaidDate(),
                orderJpaEntity.getCreatedAt()
        );
    }
}
