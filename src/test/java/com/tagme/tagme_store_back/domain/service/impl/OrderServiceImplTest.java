package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.EpsteinFiles.http.exception.ApiNotWorkingException;
import com.tagme.tagme_store_back.EpsteinFiles.payment.CreditCardPaymentService;
import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.PaymentInfoDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.repository.OrderRepository;
import com.tagme.tagme_store_back.domain.repository.PaymentInfoRepository;
import com.tagme.tagme_store_back.domain.service.UserService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private CreditCardPaymentService creditCardPaymentService;

    @Mock
    private PaymentInfoRepository paymentInfoRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private UserDto userDto;
    private OrderDto processingOrderDto;
    private OrderDto payedOrderDto;
    private PaymentInfoDto paymentInfoDto;

    @BeforeEach
    void setUp() {
        userDto = Instancio.of(UserDto.class)
                .set(field(UserDto::id), 1L)
                .create();

        processingOrderDto = new OrderDto(
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

        payedOrderDto = new OrderDto(
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

        paymentInfoDto = new PaymentInfoDto(
                1L,
                1L,
                "4111111111111111",
                "Test User",
                "123",
                "12/30"
        );
    }

    @Nested
    class GetOrdersByUserIdTests {
        @DisplayName("Given valid userId, when getOrdersByUserId is called, then return orders")
        @Test
        void getOrdersByUserIdSuccess() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getOrdersByUserId(1L)).thenReturn(List.of(processingOrderDto));

            List<OrderDto> result = orderService.getOrdersByUserId(1L);

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(orderRepository).getOrdersByUserId(1L);
        }

        @DisplayName("Given null userId, when getOrdersByUserId is called, then throw ValidationException")
        @Test
        void getOrdersByUserIdNullId() {
            assertThrows(ValidationException.class, () -> orderService.getOrdersByUserId(null));
        }

        @DisplayName("Given invalid userId, when getOrdersByUserId is called, then throw ValidationException")
        @Test
        void getOrdersByUserIdInvalidId() {
            assertThrows(ValidationException.class, () -> orderService.getOrdersByUserId(0L));
            assertThrows(ValidationException.class, () -> orderService.getOrdersByUserId(-1L));
        }
    }

    @Nested
    class GetAllOrdersTests {
        @DisplayName("Given valid params, when getAllOrders is called, then return page of orders")
        @Test
        void getAllOrdersSuccess() {
            Page<OrderDto> page = new Page<>(List.of(processingOrderDto), 1, 10, 1L);
            when(orderRepository.getAllOrders(1, 10, null, null)).thenReturn(page);

            Page<OrderDto> result = orderService.getAllOrders(1, 10, null, null);

            assertNotNull(result);
            assertEquals(1, result.data().size());
        }

        @DisplayName("Given filter by status, when getAllOrders is called, then filter is applied")
        @Test
        void getAllOrdersWithStatusFilter() {
            Page<OrderDto> page = new Page<>(List.of(processingOrderDto), 1, 10, 1L);
            when(orderRepository.getAllOrders(1, 10, OrderStatus.PROCESSING, null)).thenReturn(page);

            Page<OrderDto> result = orderService.getAllOrders(1, 10, OrderStatus.PROCESSING, null);

            assertNotNull(result);
            verify(orderRepository).getAllOrders(1, 10, OrderStatus.PROCESSING, null);
        }

        @DisplayName("Given filter by userId, when getAllOrders is called, then filter is applied")
        @Test
        void getAllOrdersWithUserIdFilter() {
            Page<OrderDto> page = new Page<>(List.of(processingOrderDto), 1, 10, 1L);
            when(orderRepository.getAllOrders(1, 10, null, 1L)).thenReturn(page);

            Page<OrderDto> result = orderService.getAllOrders(1, 10, null, 1L);

            assertNotNull(result);
            verify(orderRepository).getAllOrders(1, 10, null, 1L);
        }

        @DisplayName("Given invalid page, when getAllOrders is called, then throw ValidationException")
        @Test
        void getAllOrdersInvalidPage() {
            assertThrows(ValidationException.class, () -> orderService.getAllOrders(0, 10, null, null));
            assertThrows(ValidationException.class, () -> orderService.getAllOrders(-1, 10, null, null));
        }

        @DisplayName("Given invalid size, when getAllOrders is called, then throw ValidationException")
        @Test
        void getAllOrdersInvalidSize() {
            assertThrows(ValidationException.class, () -> orderService.getAllOrders(1, 0, null, null));
            assertThrows(ValidationException.class, () -> orderService.getAllOrders(1, -1, null, null));
        }
    }

    @Nested
    class RetryPaymentTests {
        @DisplayName("Given valid orderId with PROCESSING status, when retryPayment is called and payment succeeds, then order is PAYED")
        @Test
        void retryPaymentSuccess() {
            when(orderRepository.getById(1L)).thenReturn(Optional.of(processingOrderDto));
            when(paymentInfoRepository.findByOrderId(1L)).thenReturn(Optional.of(paymentInfoDto));
            doNothing().when(creditCardPaymentService).execute(any(), any());
            when(orderRepository.save(any())).thenReturn(payedOrderDto);

            assertDoesNotThrow(() -> orderService.retryPayment(1L));
            verify(orderRepository).save(argThat(order -> order.orderStatus() == OrderStatus.PAYED));
            verify(paymentInfoRepository).deleteByOrderId(1L);
        }

        @DisplayName("Given null orderId, when retryPayment is called, then throw ValidationException")
        @Test
        void retryPaymentNullId() {
            assertThrows(ValidationException.class, () -> orderService.retryPayment(null));
        }

        @DisplayName("Given invalid orderId, when retryPayment is called, then throw ValidationException")
        @Test
        void retryPaymentInvalidId() {
            assertThrows(ValidationException.class, () -> orderService.retryPayment(0L));
            assertThrows(ValidationException.class, () -> orderService.retryPayment(-1L));
        }

        @DisplayName("Given non-existing orderId, when retryPayment is called, then throw ResourceNotFoundException")
        @Test
        void retryPaymentOrderNotFound() {
            when(orderRepository.getById(999L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> orderService.retryPayment(999L));
        }

        @DisplayName("Given order not in PROCESSING status, when retryPayment is called, then throw BusinessException")
        @Test
        void retryPaymentNotProcessing() {
            when(orderRepository.getById(1L)).thenReturn(Optional.of(payedOrderDto));

            assertThrows(BusinessException.class, () -> orderService.retryPayment(1L));
        }

        @DisplayName("Given order without payment info, when retryPayment is called, then throw BusinessException")
        @Test
        void retryPaymentNoPaymentInfo() {
            when(orderRepository.getById(1L)).thenReturn(Optional.of(processingOrderDto));
            when(paymentInfoRepository.findByOrderId(1L)).thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> orderService.retryPayment(1L));
        }

        @DisplayName("Given other exception, when retryPayment is called, then order is CANCELLED and throw BusinessException")
        @Test
        void retryPaymentOtherException() {
            when(orderRepository.getById(1L)).thenReturn(Optional.of(processingOrderDto));
            when(paymentInfoRepository.findByOrderId(1L)).thenReturn(Optional.of(paymentInfoDto));
            doThrow(new RuntimeException("Other error")).when(creditCardPaymentService).execute(any(), any());
            when(orderRepository.save(any())).thenReturn(processingOrderDto);

            assertThrows(BusinessException.class, () -> orderService.retryPayment(1L));
            verify(orderRepository).save(argThat(order -> order.orderStatus() == OrderStatus.CANCELLED));
            verify(paymentInfoRepository).deleteByOrderId(1L);
        }
    }

    @Nested
    class GetTotalOrdersTests {
        @DisplayName("When getTotalOrders is called, then return count")
        @Test
        void getTotalOrdersSuccess() {
            when(orderRepository.countAll()).thenReturn(10L);

            Long result = orderService.getTotalOrders();

            assertEquals(10L, result);
            verify(orderRepository).countAll();
        }
    }
}
