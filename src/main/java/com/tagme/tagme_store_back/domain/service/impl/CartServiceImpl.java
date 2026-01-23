package com.tagme.tagme_store_back.domain.service.impl;

import com.github.dockerjava.api.exception.NotFoundException;
import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.mapper.OrderMapper;
import com.tagme.tagme_store_back.domain.mapper.UserMapper;
import com.tagme.tagme_store_back.domain.model.Order;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.repository.OrderRepository;
import com.tagme.tagme_store_back.domain.service.CartService;
import com.tagme.tagme_store_back.domain.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CartServiceImpl implements CartService {
    private final OrderRepository orderRepository;
    private final UserService userService;

    public CartServiceImpl(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    @Override
    public void clearCart(Long userId) {
        OrderDto orderDto = getActiveCart(userId);

        orderRepository.delete(orderDto.id());
        createCart(userId);
    }

    @Override
    public void createCart(Long userId) {
        // Verificar si el usuario existe
        UserDto user = userService.getById(userId);

        Order newCart = new Order(
                null,
                UserMapper.fromUserDtoToUser(user),
                OrderStatus.PENDING,
                null,
                LocalDateTime.now()
        );

        OrderDto newCartDto = OrderMapper.fromOrderToOrderDto(newCart);
        orderRepository.save(newCartDto);
    }

    // Devuelve un carrito en PENDING
    @Override
    public OrderDto getActiveCart(Long userId) {
        return orderRepository.getActiveOrder(userId)
                .map(OrderMapper::fromOrderDtoToOrder)
                .map(OrderMapper::fromOrderToOrderDto)
                .orElseThrow(() -> new NotFoundException("Active cart not found for user with id: " + userId));
    }

    @Override
    public OrderStatus getCartStatus(Long orderId) {
        return orderRepository.getStatus(orderId)
                .orElseThrow(() -> new NotFoundException("Cart not found with id: " + orderId));
    }

    @Override
    public void updatePendingCart(OrderDto orderDto) {
        UserDto user = userService.getById(orderDto.user().id());
        OrderDto activeCart = getActiveCart(orderDto.user().id());

        // Para que pase por la lógica de negocio
        Order orderModel = OrderMapper.fromOrderDtoToOrder(orderDto);
        OrderDto orderToUpdate = OrderMapper.fromOrderToOrderDto(orderModel);

        OrderDto updatedCart = new OrderDto(
                activeCart.id(),
                user,
                OrderStatus.PENDING,
                orderToUpdate.orderItems(),
                orderToUpdate.totalPrice(),
                activeCart.createdAt()
        );

        orderRepository.save(updatedCart);
    }

    @Override
    public void updateCart(OrderDto orderDto) {
        UserDto user = userService.getById(orderDto.user().id());
        OrderDto activeCart = getActiveCart(orderDto.user().id());

        // Para que pase por la lógica de negocio
        Order orderModel = OrderMapper.fromOrderDtoToOrder(orderDto);
        OrderDto orderToUpdate = OrderMapper.fromOrderToOrderDto(orderModel);

        OrderDto updatedCart = new OrderDto(
                activeCart.id(),
                user,
                orderToUpdate.orderStatus(),
                orderToUpdate.orderItems(),
                orderToUpdate.totalPrice(),
                activeCart.createdAt()
        );

        orderRepository.save(updatedCart);
    }
}
