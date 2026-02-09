package com.tagme.tagme_store_back.controller;

import com.tagme.tagme_store_back.controller.mapper.OrderMapper;
import com.tagme.tagme_store_back.controller.mapper.PayCartMapper;
import com.tagme.tagme_store_back.controller.webModel.request.OrderUpdateRequest;
import com.tagme.tagme_store_back.controller.webModel.request.PayCartRequest;
import com.tagme.tagme_store_back.controller.webModel.response.OrderResponse;
import com.tagme.tagme_store_back.controller.webModel.response.UserResponse;
import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.PayCartDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.service.CartService;
import com.tagme.tagme_store_back.domain.service.UserService;
import com.tagme.tagme_store_back.domain.validation.DtoValidator;
import com.tagme.tagme_store_back.web.context.AuthContext;
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

        OrderDto orderDto = OrderMapper.fromOrderUpdateRequestToOrderDto(request, user);

        cartService.updatePendingCart(orderDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/pay/credit-card")
    public ResponseEntity<Void> payCart(@RequestBody PayCartRequest request) {
        System.out.println(request);
        PayCartDto payCartDto = PayCartMapper.toDto(request);

        DtoValidator.validate(payCartDto);

        // Conseguir el usuario para validar que existe
        UserResponse userResponse = AuthContext.getUser();

        cartService.payWithCreditCard(userResponse.id(), payCartDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

