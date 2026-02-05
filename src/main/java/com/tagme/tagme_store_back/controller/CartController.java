package com.tagme.tagme_store_back.controller;

import com.tagme.tagme_store_back.controller.mapper.OrderMapper;
import com.tagme.tagme_store_back.controller.webModel.request.OrderUpdateRequest;
import com.tagme.tagme_store_back.controller.webModel.response.OrderResponse;
import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.service.CartService;
import com.tagme.tagme_store_back.domain.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/cart")
@CrossOrigin("*")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/active/{userId}")
    public ResponseEntity<OrderResponse> getActiveCart(@PathVariable Long userId) throws IOException {
        OrderDto activeCart = cartService.getActiveCart(userId);

        OrderResponse response = OrderMapper.fromOrderDtoToOrderResponse(activeCart);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Void> updateCart(@RequestBody OrderUpdateRequest request) {

        UserDto user = userService.getById(request.userId());
        // No validamos UserDto aquí porque ya viene del servicio validado
        // y puede tener campos opcionales como profilePicture null

        OrderDto orderDto = OrderMapper.fromOrderUpdateRequestToOrderDto(request, user);
        // No validamos OrderDto aquí porque orderStatus es null intencionalmente
        // el servicio asignará PENDING al carrito activo

        cartService.updatePendingCart(orderDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

