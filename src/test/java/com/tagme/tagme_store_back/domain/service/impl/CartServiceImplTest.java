package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.EpsteinFiles.http.exception.ApiNotWorkingException;
import com.tagme.tagme_store_back.EpsteinFiles.payment.CreditCardPaymentService;
import com.tagme.tagme_store_back.EpsteinFiles.payment.records.CreditCardRequest;
import com.tagme.tagme_store_back.domain.dto.*;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.repository.OrderRepository;
import com.tagme.tagme_store_back.domain.repository.PaymentInfoRepository;
import com.tagme.tagme_store_back.domain.service.ProductService;
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
class CartServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private CreditCardPaymentService creditCardPaymentService;

    @Mock
    private PaymentInfoRepository paymentInfoRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private UserDto userDto;
    private OrderDto pendingOrderDto;
    private ProductDto productDto;
    private OrderItemDto orderItemDto;

    @BeforeEach
    void setUp() {
        userDto = Instancio.of(UserDto.class)
                .set(field(UserDto::id), 1L)
                .create();

        // Crear CategoryDto con Instancio
        CategoryDto categoryDto = Instancio.of(CategoryDto.class).create();

        // Crear ProductDto manualmente para evitar problemas con Blob
        productDto = new ProductDto(
                1L,
                "Test Product",
                "Test Description",
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(90),
                null,  // Blob image
                "test.jpg",
                categoryDto,
                null  // ProductMaterial
        );

        orderItemDto = new OrderItemDto(
                1L,
                productDto,
                productDto.name(),
                productDto.image(),
                productDto.imageName(),
                2L,
                productDto.basePrice(),
                productDto.discountPercentage(),
                BigDecimal.valueOf(180)
        );

        pendingOrderDto = new OrderDto(
                1L,
                userDto,
                OrderStatus.PENDING,
                List.of(orderItemDto),
                BigDecimal.valueOf(180),
                BigDecimal.valueOf(5),
                null,
                null,
                LocalDateTime.now()
        );
    }

    @Nested
    class CreateCartTests {
        @DisplayName("Given valid userId, when createCart is called, then cart is created")
        @Test
        void createCartSuccess() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.save(any())).thenReturn(pendingOrderDto);

            assertDoesNotThrow(() -> cartService.createCart(1L));
            verify(orderRepository).save(any());
        }

        @DisplayName("Given null userId, when createCart is called, then throw ValidationException")
        @Test
        void createCartNullUserId() {
            assertThrows(ValidationException.class, () -> cartService.createCart(null));
        }

        @DisplayName("Given zero userId, when createCart is called, then throw ValidationException")
        @Test
        void createCartZeroUserId() {
            assertThrows(ValidationException.class, () -> cartService.createCart(0L));
        }

        @DisplayName("Given negative userId, when createCart is called, then throw ValidationException")
        @Test
        void createCartNegativeUserId() {
            assertThrows(ValidationException.class, () -> cartService.createCart(-1L));
        }
    }

    @Nested
    class GetActiveCartTests {
        @DisplayName("Given user with existing cart, when getActiveCart is called, then return cart")
        @Test
        void getActiveCartExists() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));

            OrderDto result = cartService.getActiveCart(1L);

            assertNotNull(result);
            assertEquals(OrderStatus.PENDING, result.orderStatus());
        }

        @DisplayName("Given user without cart, when getActiveCart is called, then create and return new cart")
        @Test
        void getActiveCartCreateNew() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L))
                    .thenReturn(Optional.empty())
                    .thenReturn(Optional.of(pendingOrderDto));
            when(orderRepository.save(any())).thenReturn(pendingOrderDto);

            OrderDto result = cartService.getActiveCart(1L);

            assertNotNull(result);
            verify(orderRepository).save(any());
        }

        @DisplayName("Given null userId, when getActiveCart is called, then throw ValidationException")
        @Test
        void getActiveCartNullUserId() {
            assertThrows(ValidationException.class, () -> cartService.getActiveCart(null));
        }

        @DisplayName("Given invalid userId, when getActiveCart is called, then throw ValidationException")
        @Test
        void getActiveCartInvalidUserId() {
            assertThrows(ValidationException.class, () -> cartService.getActiveCart(0L));
            assertThrows(ValidationException.class, () -> cartService.getActiveCart(-1L));
        }
    }

    @Nested
    class GetCartStatusTests {
        @DisplayName("Given valid orderId, when getCartStatus is called, then return status")
        @Test
        void getCartStatusSuccess() {
            when(orderRepository.getStatus(1L)).thenReturn(Optional.of(OrderStatus.PENDING));

            OrderStatus status = cartService.getCartStatus(1L);

            assertEquals(OrderStatus.PENDING, status);
        }

        @DisplayName("Given non-existing orderId, when getCartStatus is called, then throw ResourceNotFoundException")
        @Test
        void getCartStatusNotFound() {
            when(orderRepository.getStatus(999L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> cartService.getCartStatus(999L));
        }

        @DisplayName("Given null orderId, when getCartStatus is called, then throw ValidationException")
        @Test
        void getCartStatusNullId() {
            assertThrows(ValidationException.class, () -> cartService.getCartStatus(null));
        }

        @DisplayName("Given invalid orderId, when getCartStatus is called, then throw ValidationException")
        @Test
        void getCartStatusInvalidId() {
            assertThrows(ValidationException.class, () -> cartService.getCartStatus(0L));
            assertThrows(ValidationException.class, () -> cartService.getCartStatus(-1L));
        }
    }

    @Nested
    class UpdatePendingCartTests {
        @DisplayName("Given valid orderDto with PENDING status, when updatePendingCart is called, then cart is updated")
        @Test
        void updatePendingCartSuccess() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));
            when(productService.getById(anyLong())).thenReturn(productDto);
            when(orderRepository.save(any())).thenReturn(pendingOrderDto);

            assertDoesNotThrow(() -> cartService.updatePendingCart(pendingOrderDto));
            verify(orderRepository).save(any());
        }

        @DisplayName("Given null orderDto, when updatePendingCart is called, then throw ValidationException")
        @Test
        void updatePendingCartNullOrder() {
            assertThrows(ValidationException.class, () -> cartService.updatePendingCart(null));
        }

        @DisplayName("Given orderDto with null user, when updatePendingCart is called, then throw ValidationException")
        @Test
        void updatePendingCartNullUser() {
            OrderDto orderWithNullUser = new OrderDto(1L, null, OrderStatus.PENDING, List.of(), BigDecimal.ZERO, null, null, null, LocalDateTime.now());
            assertThrows(ValidationException.class, () -> cartService.updatePendingCart(orderWithNullUser));
        }

        @DisplayName("Given cart not in PENDING status, when updatePendingCart is called, then throw BusinessException")
        @Test
        void updatePendingCartNotPending() {
            OrderDto processingOrder = new OrderDto(1L, userDto, OrderStatus.PROCESSING, List.of(), BigDecimal.valueOf(100), null, null, null, LocalDateTime.now());
            
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(processingOrder));

            assertThrows(BusinessException.class, () -> cartService.updatePendingCart(pendingOrderDto));
        }

        @DisplayName("Given duplicate products in cart, when updatePendingCart is called, then throw ValidationException")
        @Test
        void updatePendingCartDuplicateProducts() {
            OrderItemDto duplicateItem = new OrderItemDto(2L, productDto, productDto.name(), productDto.image(), productDto.imageName(), 1L, productDto.basePrice(), productDto.discountPercentage(), BigDecimal.valueOf(90));
            OrderDto orderWithDuplicates = new OrderDto(1L, userDto, OrderStatus.PENDING, List.of(orderItemDto, duplicateItem), BigDecimal.valueOf(270), null, null, null, LocalDateTime.now());

            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));

            assertThrows(ValidationException.class, () -> cartService.updatePendingCart(orderWithDuplicates));
        }

        @DisplayName("Given item with invalid quantity, when updatePendingCart is called, then throw ValidationException")
        @Test
        void updatePendingCartInvalidQuantity() {
            OrderItemDto invalidItem = new OrderItemDto(1L, productDto, productDto.name(), productDto.image(), productDto.imageName(), 0L, productDto.basePrice(), productDto.discountPercentage(), BigDecimal.ZERO);
            OrderDto orderWithInvalidQuantity = new OrderDto(1L, userDto, OrderStatus.PENDING, List.of(invalidItem), BigDecimal.ZERO, null, null, null, LocalDateTime.now());

            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));

            assertThrows(ValidationException.class, () -> cartService.updatePendingCart(orderWithInvalidQuantity));
        }
    }

    @Nested
    class UpdateCartTests {
        @DisplayName("Given valid orderDto, when updateCart is called, then cart is updated")
        @Test
        void updateCartSuccess() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));
            when(productService.getById(anyLong())).thenReturn(productDto);
            when(orderRepository.save(any())).thenReturn(pendingOrderDto);

            assertDoesNotThrow(() -> cartService.updateCart(pendingOrderDto));
            verify(orderRepository).save(any());
        }

        @DisplayName("Given null orderDto, when updateCart is called, then throw ValidationException")
        @Test
        void updateCartNullOrder() {
            assertThrows(ValidationException.class, () -> cartService.updateCart(null));
        }

        @DisplayName("Given cart with PAYED status, when updateCart is called, then throw BusinessException")
        @Test
        void updateCartAlreadyPayed() {
            OrderDto payedOrder = new OrderDto(1L, userDto, OrderStatus.PAYED, List.of(), BigDecimal.valueOf(100), null, null, LocalDateTime.now(), LocalDateTime.now());
            
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(payedOrder));

            assertThrows(BusinessException.class, () -> cartService.updateCart(pendingOrderDto));
        }

        @DisplayName("Given invalid status transition PENDING to PAYED, when updateCart is called, then throw BusinessException")
        @Test
        void updateCartInvalidTransitionPendingToPayed() {
            OrderDto orderToPay = new OrderDto(1L, userDto, OrderStatus.PAYED, List.of(), BigDecimal.valueOf(100), null, null, null, LocalDateTime.now());
            
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));

            assertThrows(BusinessException.class, () -> cartService.updateCart(orderToPay));
        }

        @DisplayName("Given invalid status transition PROCESSING to PENDING, when updateCart is called, then throw BusinessException")
        @Test
        void updateCartInvalidTransitionProcessingToPending() {
            OrderDto processingOrder = new OrderDto(1L, userDto, OrderStatus.PROCESSING, List.of(), BigDecimal.valueOf(100), null, null, null, LocalDateTime.now());
            OrderDto orderToPending = new OrderDto(1L, userDto, OrderStatus.PENDING, List.of(), BigDecimal.valueOf(100), null, null, null, LocalDateTime.now());
            
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(processingOrder));

            assertThrows(BusinessException.class, () -> cartService.updateCart(orderToPending));
        }
    }

    @Nested
    class ClearCartTests {
        @DisplayName("Given user with pending cart, when clearCart is called, then cart is cleared")
        @Test
        void clearCartSuccess() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));
            when(orderRepository.save(any())).thenReturn(pendingOrderDto);

            assertDoesNotThrow(() -> cartService.clearCart(1L));
            verify(orderRepository).save(any());
        }

        @DisplayName("Given null userId, when clearCart is called, then throw ValidationException")
        @Test
        void clearCartNullUserId() {
            assertThrows(ValidationException.class, () -> cartService.clearCart(null));
        }

        @DisplayName("Given invalid userId, when clearCart is called, then throw ValidationException")
        @Test
        void clearCartInvalidUserId() {
            assertThrows(ValidationException.class, () -> cartService.clearCart(0L));
            assertThrows(ValidationException.class, () -> cartService.clearCart(-1L));
        }

        @DisplayName("Given cart not in PENDING status, when clearCart is called, then throw BusinessException")
        @Test
        void clearCartNotPending() {
            OrderDto processingOrder = new OrderDto(1L, userDto, OrderStatus.PROCESSING, List.of(), BigDecimal.valueOf(100), null, null, null, LocalDateTime.now());
            
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(processingOrder));

            assertThrows(BusinessException.class, () -> cartService.clearCart(1L));
        }
    }

    @Nested
    class PayWithCreditCardTests {
        private PayCartDto payCartDto;
        private CreditCardDto creditCardDto;
        private OrderInfoDto orderInfoDto;
        private ShippingInfoDto shippingInfoDto;

        @BeforeEach
        void setUp() {
            creditCardDto = new CreditCardDto("4111111111111111", "Test User", "123", "12/30");
            orderInfoDto = new OrderInfoDto(BigDecimal.valueOf(5), BigDecimal.valueOf(175), BigDecimal.valueOf(180));
            shippingInfoDto = Instancio.of(ShippingInfoDto.class).create();
            payCartDto = new PayCartDto(orderInfoDto, creditCardDto, shippingInfoDto);
        }

        @DisplayName("Given valid payment, when payWithCreditCard is called, then payment succeeds")
        @Test
        void payWithCreditCardSuccess() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));
            when(orderRepository.save(any())).thenReturn(pendingOrderDto);
            when(paymentInfoRepository.save(any())).thenReturn(null);
            doNothing().when(creditCardPaymentService).execute(any(), any());

            assertDoesNotThrow(() -> cartService.payWithCreditCard(1L, payCartDto));
            verify(creditCardPaymentService).execute(any(), any());
            verify(paymentInfoRepository).deleteByOrderId(any());
        }

        @DisplayName("Given null userId, when payWithCreditCard is called, then throw ValidationException")
        @Test
        void payWithCreditCardNullUserId() {
            assertThrows(ValidationException.class, () -> cartService.payWithCreditCard(null, payCartDto));
        }

        @DisplayName("Given null payCartDto, when payWithCreditCard is called, then throw ValidationException")
        @Test
        void payWithCreditCardNullPayCartDto() {
            assertThrows(ValidationException.class, () -> cartService.payWithCreditCard(1L, null));
        }

        @DisplayName("Given API error, when payWithCreditCard is called, then order stays in PROCESSING")
        @Test
        void payWithCreditCardApiError() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));
            when(orderRepository.save(any())).thenReturn(pendingOrderDto);
            when(paymentInfoRepository.save(any())).thenReturn(null);
            doThrow(new ApiNotWorkingException("API Error")).when(creditCardPaymentService).execute(any(), any());

            assertDoesNotThrow(() -> cartService.payWithCreditCard(1L, payCartDto));
            verify(paymentInfoRepository, never()).deleteByOrderId(any());
        }

        @DisplayName("Given other exception, when payWithCreditCard is called, then throw RuntimeException")
        @Test
        void payWithCreditCardOtherException() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));
            when(orderRepository.save(any())).thenReturn(pendingOrderDto);
            when(paymentInfoRepository.save(any())).thenReturn(null);
            doThrow(new RuntimeException("Other error")).when(creditCardPaymentService).execute(any(), any());

            assertThrows(RuntimeException.class, () -> cartService.payWithCreditCard(1L, payCartDto));
        }
    }
}
