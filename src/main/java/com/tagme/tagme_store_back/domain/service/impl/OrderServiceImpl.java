package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.mapper.OrderMapper;
import com.tagme.tagme_store_back.domain.mapper.UserMapper;
import com.tagme.tagme_store_back.domain.model.Order;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.model.User;
import com.tagme.tagme_store_back.domain.repository.OrderRepository;
import com.tagme.tagme_store_back.domain.repository.UserRepository;
import com.tagme.tagme_store_back.domain.service.OrderService;
import jakarta.transaction.Transactional;

public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public OrderDto create(OrderDto orderDto) {
        Order order = OrderMapper.fromOrderDtoToOrder(orderDto);
        OrderDto orderToCreate = OrderMapper.fromOrderToOrderDto(order);
        return orderRepository.save(orderToCreate);
    }

    @Override
    @Transactional
    public OrderDto update(OrderDto orderDto) {
        Order order = OrderMapper.fromOrderDtoToOrder(orderDto);

        if (getStatus(orderDto) == null) {
            throw new ResourceNotFoundException("Order not found with id: " + order.getId());
        } else if (getStatus(orderDto) != OrderStatus.PENDING) {
            throw new BusinessException("Order status is not PENDING");
        }

        OrderDto orderToUpdate = OrderMapper.fromOrderToOrderDto(order);
        return orderRepository.save(orderToUpdate);
    }

    @Override
    @Transactional
    public void delete(Long orderId) {
        if (orderRepository.getStatus(orderId).isEmpty()) {
            throw new RuntimeException("The order with id " + orderId + " does not exist.");
        }

        orderRepository.delete(orderId);
    }

    @Override
    public OrderStatus getStatus(OrderDto orderDto) {
        return orderRepository.getStatus(orderDto.id())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderDto.id()));
    }

    @Override
    public OrderDto getActiveOrder(UserDto userDto) {

        return orderRepository.getActiveOrder(userDto.id())
                .orElseGet(() -> {

                    User user = userRepository.findById(userDto.id())
                            .map(UserMapper::fromUserDtoToUser)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "User not found with id: " + userDto.id()));

                    Order newOrder = new Order(
                            null,
                            user,
                            OrderStatus.PENDING,
                            null,
                            null
                    );

                    OrderDto orderDto = OrderMapper.fromOrderToOrderDto(newOrder);
                    return orderRepository.save(orderDto);
                });
    }

}
