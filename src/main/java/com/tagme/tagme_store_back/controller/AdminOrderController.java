package com.tagme.tagme_store_back.controller;

import com.tagme.tagme_store_back.controller.mapper.OrderMapper;
import com.tagme.tagme_store_back.controller.webModel.response.OrderResponse;
import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/orders")
@CrossOrigin("*")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long userId
    ) throws IOException {
        OrderStatus orderStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                orderStatus = OrderStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Invalid status, ignore filter
            }
        }

        Page<OrderDto> ordersPage = orderService.getAllOrders(page, size, orderStatus, userId);

        List<OrderResponse> orderResponses = ordersPage.data().stream()
                .map(orderDto -> {
                    try {
                        return OrderMapper.fromOrderDtoToOrderResponse(orderDto);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        Page<OrderResponse> response = new Page<>(
                orderResponses,
                ordersPage.pageNumber(),
                ordersPage.pageSize(),
                ordersPage.totalElements(),
                ordersPage.totalPages()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{orderId}/retry")
    public ResponseEntity<Void> retryPayment(@PathVariable Long orderId) {
        orderService.retryPayment(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalOrders() {
        return new ResponseEntity<>(orderService.getTotalOrders(), HttpStatus.OK);
    }
}
