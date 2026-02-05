package com.tagme.tagme_store_back.domain.mapper;


import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.OrderItemDto;
import com.tagme.tagme_store_back.domain.model.Order;
import com.tagme.tagme_store_back.domain.model.OrderItem;
import com.tagme.tagme_store_back.domain.model.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    public static Order fromOrderDtoToOrder(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }

        List<OrderItem> orderItems = new ArrayList<>();
        if (orderDto.orderItems() != null) {
            orderItems = orderDto.orderItems().stream()
                    .map(OrderItemMapper::fromOrderItemDtoToOrderItem)
                    .toList();
        }

        return new Order(
                orderDto.id(),
                UserMapper.fromUserDtoToUser(orderDto.user()),
                orderDto.orderStatus(),
                orderItems,
                orderDto.paidDate(),
                orderDto.createdAt()
        );
    }

    /**
     * Mapea Order a OrderDto considerando el estado del pedido:
     * - PENDING: Los items usan datos actuales del producto
     * - PAYED/PROCESSING: Los items usan el snapshot guardado
     */
    public static OrderDto fromOrderToOrderDto(Order order) {
        if (order == null) {
            return null;
        }

        OrderStatus orderStatus = order.getOrderStatus();
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        if (order.getOrderItems() != null) {
            orderItemDtos = order.getOrderItems().stream()
                    .map(item -> OrderItemMapper.fromOrderItemToOrderItemDto(item, orderStatus))
                    .toList();
        }

        return new OrderDto(
                order.getId(),
                UserMapper.fromUserToUserDto(order.getUser()),
                order.getOrderStatus(),
                orderItemDtos,
                order.getTotalPrice(),
                order.getPaidDate(),
                order.getCreatedAt()
        );
    }
}
