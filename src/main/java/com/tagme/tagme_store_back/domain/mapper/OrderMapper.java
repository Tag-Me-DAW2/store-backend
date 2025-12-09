package com.tagme.tagme_store_back.domain.mapper;


import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.OrderItemDto;
import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.model.Order;
import com.tagme.tagme_store_back.domain.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    public static Order fromOrderDtoToOrder(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }

        List<OrderItem> productDtos = new ArrayList<>();
        if (orderDto.orderItems() != null) {
            productDtos = orderDto.orderItems().stream()
                    .map(OrderItemMapper::fromOrderItemDtoToOrderItem)
                    .toList();
        }

        return new Order(
                orderDto.id(),
                UserMapper.fromUserDtoToUser(orderDto.user()),
                orderDto.orderStatus(),
                productDtos,
                orderDto.createdAt()
        );
    }

    public static OrderDto fromOrderToOrderDto(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        if (order.getOrderItems() != null) {
            orderItemDtos = order.getOrderItems().stream()
                    .map(OrderItemMapper::fromOrderItemToOrderItemDto)
                    .toList();
        }

        return new OrderDto(
                order.getId(),
                UserMapper.fromUserToUserDto(order.getUser()),
                order.getOrderStatus(),
                orderItemDtos,
                order.getTotalPrice(),
                order.getCreatedAt()
        );
    }
}
