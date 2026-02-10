package com.tagme.tagme_store_back.controller;

import com.tagme.tagme_store_back.controller.mapper.OrderMapper;
import com.tagme.tagme_store_back.controller.webModel.response.OrderResponse;
import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) throws IOException {
        List<OrderDto> orders = orderService.getOrdersByUserId(userId);

        List<OrderResponse> response = orders.stream()
                .map(orderDto -> {
                    try {
                        return OrderMapper.fromOrderDtoToOrderResponse(orderDto);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
