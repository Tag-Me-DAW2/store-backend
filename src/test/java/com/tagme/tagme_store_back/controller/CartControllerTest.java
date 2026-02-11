package com.tagme.tagme_store_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tagme.tagme_store_back.controller.webModel.request.*;
import com.tagme.tagme_store_back.controller.webModel.response.UserResponse;
import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.service.AuthService;
import com.tagme.tagme_store_back.domain.service.CartService;
import com.tagme.tagme_store_back.domain.service.UserService;
import com.tagme.tagme_store_back.web.context.AuthContext;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.instancio.Select.field;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class GetActiveCartTests {
        @DisplayName("Given valid userId, when getActiveCart is called, then return cart")
        @Test
        void getActiveCartSuccess() throws Exception {
            UserDto userDto = Instancio.of(UserDto.class)
                    .set(field(UserDto::id), 1L)
                    .create();

            OrderDto orderDto = new OrderDto(
                    1L,
                    userDto,
                    OrderStatus.PENDING,
                    new ArrayList<>(),
                    BigDecimal.ZERO,
                    null,
                    null,
                    null,
                    LocalDateTime.now()
            );

            when(cartService.getActiveCart(1L)).thenReturn(orderDto);

            mockMvc.perform(get("/cart/active/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.orderStatus").value("PENDING"));
        }

        @DisplayName("Given non-existing userId, when getActiveCart is called, then return 404")
        @Test
        void getActiveCartUserNotFound() throws Exception {
            when(cartService.getActiveCart(999L)).thenThrow(new ResourceNotFoundException("User not found"));

            mockMvc.perform(get("/cart/active/999"))
                    .andExpect(status().isNotFound());
        }

        @DisplayName("Given invalid userId, when getActiveCart is called, then return 400")
        @Test
        void getActiveCartInvalidUserId() throws Exception {
            when(cartService.getActiveCart(-1L)).thenThrow(new ValidationException("User ID must be a positive number"));

            mockMvc.perform(get("/cart/active/-1"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateCartTests {
        @DisplayName("Given valid request, when updateCart is called, then return 200")
        @Test
        void updateCartSuccess() throws Exception {
            UserDto userDto = Instancio.of(UserDto.class)
                    .set(field(UserDto::id), 1L)
                    .create();

            List<OrderItemRequest> items = new ArrayList<>();
            items.add(new OrderItemRequest(1L, 2L));
            
            OrderUpdateRequest request = new OrderUpdateRequest(
                    1L,
                    items
            );

            when(userService.getById(1L)).thenReturn(userDto);
            doNothing().when(cartService).updatePendingCart(any());

            mockMvc.perform(put("/cart")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @DisplayName("Given non-existing user, when updateCart is called, then return 404")
        @Test
        void updateCartUserNotFound() throws Exception {
            List<OrderItemRequest> items = new ArrayList<>();
            items.add(new OrderItemRequest(1L, 2L));
            
            OrderUpdateRequest request = new OrderUpdateRequest(
                    999L,
                    items
            );

            when(userService.getById(999L)).thenThrow(new ResourceNotFoundException("User not found"));

            mockMvc.perform(put("/cart")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @DisplayName("Given cart not pending, when updateCart is called, then return 400")
        @Test
        void updateCartNotPending() throws Exception {
            UserDto userDto = Instancio.of(UserDto.class)
                    .set(field(UserDto::id), 1L)
                    .create();

            List<OrderItemRequest> items = new ArrayList<>();
            items.add(new OrderItemRequest(1L, 2L));
            
            OrderUpdateRequest request = new OrderUpdateRequest(
                    1L,
                    items
            );

            when(userService.getById(1L)).thenReturn(userDto);
            doThrow(new BusinessException("Cannot update cart")).when(cartService).updatePendingCart(any());

            mockMvc.perform(put("/cart")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class PayCartTests {
        @DisplayName("Given valid payment, when payCart is called, then return 200")
        @Test
        void payCartSuccess() throws Exception {
            UserResponse userResponse = Instancio.of(UserResponse.class)
                    .set(field(UserResponse::id), 1L)
                    .create();

            PayCartRequest request = new PayCartRequest(
                    new OrderInfoRequest(BigDecimal.valueOf(5), BigDecimal.valueOf(95), BigDecimal.valueOf(100)),
                    new CreditCardRequest("4111111111111111", "Test User", "123", "12/30"),
                    new ShippingInfoRequest("John", "Doe", "test@example.com", "555-1234", "Address", "City", "12345", "Country")
            );

            try (MockedStatic<AuthContext> mockedStatic = mockStatic(AuthContext.class)) {
                mockedStatic.when(AuthContext::getUser).thenReturn(userResponse);
                doNothing().when(cartService).payWithCreditCard(anyLong(), any());

                mockMvc.perform(post("/cart/pay/credit-card")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk());
            }
        }

        @DisplayName("Given payment error, when payCart is called, then return 400")
        @Test
        void payCartError() throws Exception {
            UserResponse userResponse = Instancio.of(UserResponse.class)
                    .set(field(UserResponse::id), 1L)
                    .create();

            PayCartRequest request = new PayCartRequest(
                    new OrderInfoRequest(BigDecimal.valueOf(5), BigDecimal.valueOf(95), BigDecimal.valueOf(100)),
                    new CreditCardRequest("4111111111111111", "Test User", "123", "12/30"),
                    new ShippingInfoRequest("John", "Doe", "test@example.com", "555-1234", "Address", "City", "12345", "Country")
            );

            try (MockedStatic<AuthContext> mockedStatic = mockStatic(AuthContext.class)) {
                mockedStatic.when(AuthContext::getUser).thenReturn(userResponse);
                doThrow(new RuntimeException("Payment failed")).when(cartService).payWithCreditCard(anyLong(), any());

                mockMvc.perform(post("/cart/pay/credit-card")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isInternalServerError());
            }
        }
    }
}
