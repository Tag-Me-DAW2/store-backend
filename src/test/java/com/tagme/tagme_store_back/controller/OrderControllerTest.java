package com.tagme.tagme_store_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.service.AuthService;
import com.tagme.tagme_store_back.domain.service.OrderService;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class GetOrdersByUserIdTests {
        @DisplayName("Given valid userId, when getOrdersByUserId is called, then return orders")
        @Test
        void getOrdersByUserIdSuccess() throws Exception {
            UserDto userDto = Instancio.of(UserDto.class)
                    .set(field(UserDto::id), 1L)
                    .create();

            OrderDto orderDto = new OrderDto(
                    1L,
                    userDto,
                    OrderStatus.PAYED,
                    new ArrayList<>(),
                    BigDecimal.valueOf(100),
                    BigDecimal.valueOf(5),
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            when(orderService.getOrdersByUserId(1L)).thenReturn(List.of(orderDto));

            mockMvc.perform(get("/orders/user/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].orderStatus").value("PAYED"));
        }

        @DisplayName("Given userId with no orders, when getOrdersByUserId is called, then return empty list")
        @Test
        void getOrdersByUserIdEmpty() throws Exception {
            when(orderService.getOrdersByUserId(1L)).thenReturn(List.of());

            mockMvc.perform(get("/orders/user/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }

        @DisplayName("Given non-existing userId, when getOrdersByUserId is called, then return 404")
        @Test
        void getOrdersByUserIdNotFound() throws Exception {
            when(orderService.getOrdersByUserId(999L)).thenThrow(new ResourceNotFoundException("User not found"));

            mockMvc.perform(get("/orders/user/999"))
                    .andExpect(status().isNotFound());
        }

        @DisplayName("Given invalid userId, when getOrdersByUserId is called, then return 400")
        @Test
        void getOrdersByUserIdInvalid() throws Exception {
            when(orderService.getOrdersByUserId(-1L)).thenThrow(new ValidationException("User ID must be a positive number"));

            mockMvc.perform(get("/orders/user/-1"))
                    .andExpect(status().isBadRequest());
        }
    }
}
