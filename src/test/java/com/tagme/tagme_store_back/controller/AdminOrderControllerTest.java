package com.tagme.tagme_store_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.service.AuthService;
import com.tagme.tagme_store_back.domain.service.OrderService;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

@WebMvcTest(AdminOrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminOrderControllerTest {

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class GetAllOrdersTests {
        @DisplayName("Given no filters, when getAllOrders is called, then return all orders")
        @Test
        void getAllOrdersNoFilters() throws Exception {
            UserDto userDto = Instancio.of(UserDto.class)
                    .set(field(UserDto::id), 1L)
                    .create();

            OrderDto orderDto = new OrderDto(
                    1L,
                    userDto,
                    OrderStatus.PROCESSING,
                    new ArrayList<>(),
                    BigDecimal.valueOf(100),
                    BigDecimal.valueOf(5),
                    null,
                    null,
                    LocalDateTime.now()
            );

            Page<OrderDto> page = new Page<>(List.of(orderDto), 1, 10, 1L);
            when(orderService.getAllOrders(1, 10, null, null)).thenReturn(page);

            mockMvc.perform(get("/admin/orders"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(1))
                    .andExpect(jsonPath("$.data[0].id").value(1L))
                    .andExpect(jsonPath("$.pageNumber").value(1))
                    .andExpect(jsonPath("$.pageSize").value(10));
        }

        @DisplayName("Given status filter, when getAllOrders is called, then filter by status")
        @Test
        void getAllOrdersWithStatusFilter() throws Exception {
            UserDto userDto = Instancio.of(UserDto.class)
                    .set(field(UserDto::id), 1L)
                    .create();

            OrderDto orderDto = new OrderDto(
                    1L,
                    userDto,
                    OrderStatus.PROCESSING,
                    new ArrayList<>(),
                    BigDecimal.valueOf(100),
                    BigDecimal.valueOf(5),
                    null,
                    null,
                    LocalDateTime.now()
            );

            Page<OrderDto> page = new Page<>(List.of(orderDto), 1, 10, 1L);
            when(orderService.getAllOrders(1, 10, OrderStatus.PROCESSING, null)).thenReturn(page);

            mockMvc.perform(get("/admin/orders").param("status", "PROCESSING"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(1))
                    .andExpect(jsonPath("$.data[0].orderStatus").value("PROCESSING"));
        }

        @DisplayName("Given userId filter, when getAllOrders is called, then filter by userId")
        @Test
        void getAllOrdersWithUserIdFilter() throws Exception {
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

            Page<OrderDto> page = new Page<>(List.of(orderDto), 1, 10, 1L);
            when(orderService.getAllOrders(1, 10, null, 1L)).thenReturn(page);

            mockMvc.perform(get("/admin/orders").param("userId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(1));
        }

        @DisplayName("Given custom pagination, when getAllOrders is called, then use custom values")
        @Test
        void getAllOrdersWithPagination() throws Exception {
            Page<OrderDto> page = new Page<>(List.of(), 2, 5, 0L);
            when(orderService.getAllOrders(2, 5, null, null)).thenReturn(page);

            mockMvc.perform(get("/admin/orders").param("page", "2").param("size", "5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.pageNumber").value(2))
                    .andExpect(jsonPath("$.pageSize").value(5));
        }

        @DisplayName("Given invalid status, when getAllOrders is called, then ignore filter")
        @Test
        void getAllOrdersWithInvalidStatus() throws Exception {
            Page<OrderDto> page = new Page<>(List.of(), 1, 10, 0L);
            when(orderService.getAllOrders(1, 10, null, null)).thenReturn(page);

            mockMvc.perform(get("/admin/orders").param("status", "INVALID"))
                    .andExpect(status().isOk());
        }

        @DisplayName("Given invalid pagination, when getAllOrders is called, then return 400")
        @Test
        void getAllOrdersInvalidPagination() throws Exception {
            when(orderService.getAllOrders(0, 10, null, null))
                    .thenThrow(new ValidationException("Page number must be at least 1"));

            mockMvc.perform(get("/admin/orders").param("page", "0"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class RetryPaymentTests {
        @DisplayName("Given valid orderId, when retryPayment is called, then return 200")
        @Test
        void retryPaymentSuccess() throws Exception {
            doNothing().when(orderService).retryPayment(1L);

            mockMvc.perform(post("/admin/orders/1/retry"))
                    .andExpect(status().isOk());
        }

        @DisplayName("Given non-existing orderId, when retryPayment is called, then return 404")
        @Test
        void retryPaymentOrderNotFound() throws Exception {
            doThrow(new ResourceNotFoundException("Pedido no encontrado")).when(orderService).retryPayment(999L);

            mockMvc.perform(post("/admin/orders/999/retry"))
                    .andExpect(status().isNotFound());
        }

        @DisplayName("Given order not processing, when retryPayment is called, then return 400")
        @Test
        void retryPaymentNotProcessing() throws Exception {
            doThrow(new BusinessException("Solo se pueden reintentar pedidos en estado PROCESANDO"))
                    .when(orderService).retryPayment(1L);

            mockMvc.perform(post("/admin/orders/1/retry"))
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("Given no payment info, when retryPayment is called, then return 400")
        @Test
        void retryPaymentNoPaymentInfo() throws Exception {
            doThrow(new BusinessException("No se encontraron datos de pago para este pedido"))
                    .when(orderService).retryPayment(1L);

            mockMvc.perform(post("/admin/orders/1/retry"))
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("Given payment fails, when retryPayment is called, then return 400")
        @Test
        void retryPaymentFails() throws Exception {
            doThrow(new BusinessException("El pago ha fallado. El pedido ha sido cancelado."))
                    .when(orderService).retryPayment(1L);

            mockMvc.perform(post("/admin/orders/1/retry"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class GetTotalOrdersTests {
        @DisplayName("When getTotalOrders is called, then return count")
        @Test
        void getTotalOrdersSuccess() throws Exception {
            when(orderService.getTotalOrders()).thenReturn(25L);

            mockMvc.perform(get("/admin/orders/count"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("25"));
        }
    }
}
