package com.tagme.tagme_store_back.controller;

import com.tagme.tagme_store_back.controller.mapper.CartMapper;
import com.tagme.tagme_store_back.controller.webModel.request.CartUpdateRequest;
import com.tagme.tagme_store_back.controller.webModel.response.CartResponse;
import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.ValidationException;
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

    /**
     * Obtiene el carrito activo del usuario.
     * Si el usuario no tiene un carrito activo, se crea uno automáticamente.
     *
     * @param userId ID del usuario
     * @return CartResponse con el carrito activo
     */
    @GetMapping("/active/{userId}")
    public ResponseEntity<CartResponse> getActiveCart(@PathVariable Long userId) throws IOException {
        validateUserId(userId);

        OrderDto activeCart = cartService.getActiveCart(userId);
        CartResponse response = CartMapper.fromOrderDtoToCartResponse(activeCart);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Actualiza el carrito del usuario (añade/modifica items).
     * Solo permite actualización si el carrito está en estado PENDING.
     *
     * @param request CartUpdateRequest con los items a actualizar
     * @return ResponseEntity vacío con status 200
     */
    @PutMapping
    public ResponseEntity<Void> updateCart(@RequestBody CartUpdateRequest request) {
        validateCartUpdateRequest(request);

        UserDto user = userService.getById(request.userId());
        OrderDto orderDto = CartMapper.fromCartUpdateRequestToOrderDto(request, user);
        cartService.updatePendingCart(orderDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Limpia el carrito del usuario (elimina todos los items).
     * Crea un nuevo carrito vacío para el usuario.
     *
     * @param userId ID del usuario
     * @return ResponseEntity vacío con status 200
     */
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        validateUserId(userId);

        cartService.clearCart(userId);
        return new ResponseEntity<>(HttpStatus.OK);
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

    private void validateCartUpdateRequest(CartUpdateRequest request) {
        if (request == null) {
            throw new ValidationException("Cart update request cannot be null");
        }
        validateUserId(request.userId());

        if (request.orderItems() == null) {
            throw new ValidationException("Order items list cannot be null");
        }

        // Validar cada item del carrito
        request.orderItems().forEach(item -> {
            if (item.productId() == null || item.productId() <= 0) {
                throw new ValidationException("Product ID must be a positive number");
            }
            if (item.quantity() == null || item.quantity() < 1) {
                throw new ValidationException("Quantity must be at least 1");
            }
        });
    }
}

