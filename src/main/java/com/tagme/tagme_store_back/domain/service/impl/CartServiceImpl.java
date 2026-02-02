package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.OrderItemDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.mapper.OrderMapper;
import com.tagme.tagme_store_back.domain.mapper.UserMapper;
import com.tagme.tagme_store_back.domain.model.Order;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.repository.OrderRepository;
import com.tagme.tagme_store_back.domain.service.CartService;
import com.tagme.tagme_store_back.domain.service.UserService;
import com.tagme.tagme_store_back.domain.validation.DtoValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CartServiceImpl implements CartService {
    private final OrderRepository orderRepository;
    private final UserService userService;

    public CartServiceImpl(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    @Override
    public void clearCart(Long userId) {
        validateUserId(userId);

        OrderDto orderDto = getActiveCart(userId);
        orderRepository.delete(orderDto.id());
        createCart(userId);
    }

    @Override
    public void createCart(Long userId) {
        validateUserId(userId);

        // Verificar si el usuario existe (lanza excepción si no existe)
        UserDto user = userService.getById(userId);

        Order newCart = new Order(
                null,
                UserMapper.fromUserDtoToUser(user),
                OrderStatus.PENDING,
                new ArrayList<>(),
                LocalDateTime.now()
        );

        OrderDto newCartDto = OrderMapper.fromOrderToOrderDto(newCart);
        DtoValidator.validate(newCartDto);
        orderRepository.save(newCartDto);
    }

    @Override
    public OrderDto getActiveCart(Long userId) {
        validateUserId(userId);

        // Verificar que el usuario existe
        userService.getById(userId);

        return orderRepository.getActiveOrder(userId)
                .map(OrderMapper::fromOrderDtoToOrder)
                .map(OrderMapper::fromOrderToOrderDto)
                .orElseGet(() -> {
                    createCart(userId);
                    return orderRepository.getActiveOrder(userId)
                            .map(OrderMapper::fromOrderDtoToOrder)
                            .map(OrderMapper::fromOrderToOrderDto)
                            .orElseThrow(() -> new BusinessException("Failed to create cart for user with id: " + userId));
                });
    }

    @Override
    public OrderStatus getCartStatus(Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new ValidationException("Order ID must be a positive number");
        }

        return orderRepository.getStatus(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + orderId));
    }

    @Override
    public void updatePendingCart(OrderDto orderDto) {
        validateOrderDtoForUpdate(orderDto);

        UserDto user = userService.getById(orderDto.user().id());
        OrderDto activeCart = getActiveCart(orderDto.user().id());

        // Verificar que el carrito está en estado PENDING
        if (activeCart.orderStatus() != OrderStatus.PENDING) {
            throw new BusinessException("Cannot update cart. Cart is not in PENDING status");
        }

        // Validar items del carrito
        validateOrderItems(orderDto.orderItems());

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

        DtoValidator.validate(updatedCart);
        orderRepository.save(updatedCart);
    }

    @Override
    public void updateCart(OrderDto orderDto) {
        validateOrderDtoForUpdate(orderDto);

        UserDto user = userService.getById(orderDto.user().id());
        OrderDto activeCart = getActiveCart(orderDto.user().id());

        // Validar transición de estado
        validateStatusTransition(activeCart.orderStatus(), orderDto.orderStatus());

        // Validar items del carrito
        validateOrderItems(orderDto.orderItems());

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

        DtoValidator.validate(updatedCart);
        orderRepository.save(updatedCart);
    }

    // ==================== Métodos de validación ====================

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new ValidationException("User ID cannot be null");
        }
        if (userId <= 0) {
            throw new ValidationException("User ID must be a positive number");
        }
    }

    private void validateOrderDtoForUpdate(OrderDto orderDto) {
        if (orderDto == null) {
            throw new ValidationException("Order DTO cannot be null");
        }
        if (orderDto.user() == null) {
            throw new ValidationException("User cannot be null in order");
        }
        validateUserId(orderDto.user().id());
    }

    private void validateOrderItems(java.util.List<OrderItemDto> items) {
        if (items == null) {
            return; // Lista vacía es válida (carrito vacío)
        }

        Set<Long> productIds = new HashSet<>();
        for (OrderItemDto item : items) {
            if (item.productDto() == null || item.productDto().id() == null) {
                throw new ValidationException("Product cannot be null in order item");
            }

            // Verificar productos duplicados
            if (!productIds.add(item.productDto().id())) {
                throw new BusinessException("Duplicate product in cart: " + item.productDto().id());
            }

            if (item.quantity() == null || item.quantity() < 1) {
                throw new ValidationException("Quantity must be at least 1 for product: " + item.productDto().id());
            }
        }
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (newStatus == null) {
            throw new ValidationException("New order status cannot be null");
        }

        // Definir transiciones válidas
        boolean validTransition = switch (currentStatus) {
            case PENDING -> newStatus == OrderStatus.PENDING || newStatus == OrderStatus.PROCESSING;
            case PROCESSING -> newStatus == OrderStatus.PROCESSING || newStatus == OrderStatus.PAYED;
            case PAYED -> false; // Estado final, no se puede cambiar
        };

        if (!validTransition) {
            throw new BusinessException(
                    String.format("Invalid status transition from %s to %s", currentStatus, newStatus)
            );
        }
    }
}
