package com.tagme.tagme_store_back.domain.mapper;

import com.tagme.tagme_store_back.domain.dto.*;
import com.tagme.tagme_store_back.domain.model.*;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderMapper Tests")
class OrderMapperTest {

    @Nested
    @DisplayName("fromOrderDtoToOrder Tests")
    class FromOrderDtoToOrderTests {
        @Test
        @DisplayName("Debería devolver null cuando el DTO es null")
        void fromOrderDtoToOrder_NullInput_ReturnsNull() {
            assertNull(OrderMapper.fromOrderDtoToOrder(null));
        }

        @Test
        @DisplayName("Debería mapear correctamente DTO a modelo")
        void fromOrderDtoToOrder_ShouldMapCorrectly() {
            UserDto userDto = Instancio.of(UserDto.class)
                    .withSeed(10)
                    .create();

            ShippingInfoDto shippingInfoDto = Instancio.of(ShippingInfoDto.class)
                    .withSeed(20)
                    .create();

            ProductDto productDto = Instancio.of(ProductDto.class)
                    .ignore(Select.field(ProductDto.class, "image"))
                    .withSeed(30)
                    .create();

            OrderItemDto orderItemDto = new OrderItemDto(
                    1L,
                    productDto,
                    "Test Product",
                    null,
                    "test.jpg",
                    2L,
                    new BigDecimal("100.00"),
                    new BigDecimal("10.00"),
                    new BigDecimal("180.00")
            );

            List<OrderItemDto> orderItems = List.of(orderItemDto);

            OrderDto dto = new OrderDto(
                    1L,
                    userDto,
                    OrderStatus.PENDING,
                    orderItems,
                    new BigDecimal("180.00"),
                    new BigDecimal("5.00"),
                    shippingInfoDto,
                    null,
                    LocalDateTime.now()
            );

            Order order = OrderMapper.fromOrderDtoToOrder(dto);

            assertNotNull(order);
            assertAll(
                    () -> assertEquals(dto.id(), order.getId()),
                    () -> assertEquals(dto.orderStatus(), order.getOrderStatus()),
                    () -> assertEquals(dto.shippingCost(), order.getShippingCost()),
                    () -> assertEquals(dto.paidDate(), order.getPaidDate()),
                    () -> assertEquals(dto.createdAt(), order.getCreatedAt()),
                    () -> assertNotNull(order.getUser()),
                    () -> assertNotNull(order.getShippingInfo()),
                    () -> assertEquals(1, order.getOrderItems().size())
            );
        }

        @Test
        @DisplayName("Debería mapear con lista de items null")
        void fromOrderDtoToOrder_NullOrderItems_ShouldMapWithEmptyList() {
            UserDto userDto = Instancio.of(UserDto.class)
                    .withSeed(10)
                    .create();

            OrderDto dto = new OrderDto(
                    1L,
                    userDto,
                    OrderStatus.PENDING,
                    null,
                    BigDecimal.ZERO,
                    new BigDecimal("5.00"),
                    null,
                    null,
                    LocalDateTime.now()
            );

            Order order = OrderMapper.fromOrderDtoToOrder(dto);

            assertNotNull(order);
            assertTrue(order.getOrderItems().isEmpty());
        }

        @Test
        @DisplayName("Debería mapear con shippingInfo null")
        void fromOrderDtoToOrder_NullShippingInfo_ShouldMapCorrectly() {
            UserDto userDto = Instancio.of(UserDto.class)
                    .withSeed(10)
                    .create();

            OrderDto dto = new OrderDto(
                    1L,
                    userDto,
                    OrderStatus.PENDING,
                    new ArrayList<>(),
                    BigDecimal.ZERO,
                    new BigDecimal("5.00"),
                    null,
                    null,
                    LocalDateTime.now()
            );

            Order order = OrderMapper.fromOrderDtoToOrder(dto);

            assertNotNull(order);
            assertNull(order.getShippingInfo());
        }
    }

    @Nested
    @DisplayName("fromOrderToOrderDto Tests")
    class FromOrderToOrderDtoTests {
        @Test
        @DisplayName("Debería devolver null cuando el modelo es null")
        void fromOrderToOrderDto_NullInput_ReturnsNull() {
            assertNull(OrderMapper.fromOrderToOrderDto(null));
        }

        @Test
        @DisplayName("Debería mapear correctamente modelo a DTO")
        void fromOrderToOrderDto_ShouldMapCorrectly() {
            User user = Instancio.of(User.class)
                    .withSeed(10)
                    .create();

            ShippingInfo shippingInfo = Instancio.of(ShippingInfo.class)
                    .withSeed(20)
                    .create();

            Product product = Instancio.of(Product.class)
                    .ignore(Select.field(Product.class, "image"))
                    .set(Select.field(Product.class, "basePrice"), new BigDecimal("100.00"))
                    .set(Select.field(Product.class, "discountPercentage"), new BigDecimal("10.00"))
                    .withSeed(30)
                    .create();

            OrderItem orderItem = new OrderItem(
                    1L,
                    product,
                    "Test Product",
                    "image".getBytes(),
                    "test.jpg",
                    2L,
                    new BigDecimal("100.00"),
                    new BigDecimal("10.00")
            );

            List<OrderItem> orderItems = List.of(orderItem);

            Order order = new Order(
                    1L,
                    user,
                    OrderStatus.PAYED,
                    orderItems,
                    new BigDecimal("5.00"),
                    shippingInfo,
                    LocalDateTime.now(),
                    LocalDateTime.now().minusDays(1)
            );

            OrderDto dto = OrderMapper.fromOrderToOrderDto(order);

            assertNotNull(dto);
            assertAll(
                    () -> assertEquals(order.getId(), dto.id()),
                    () -> assertEquals(order.getOrderStatus(), dto.orderStatus()),
                    () -> assertEquals(order.getShippingCost(), dto.shippingCost()),
                    () -> assertEquals(order.getPaidDate(), dto.paidDate()),
                    () -> assertEquals(order.getCreatedAt(), dto.createdAt()),
                    () -> assertNotNull(dto.user()),
                    () -> assertNotNull(dto.shippingInfo()),
                    () -> assertEquals(1, dto.orderItems().size())
            );
        }

        @Test
        @DisplayName("Debería mapear con lista de items null")
        void fromOrderToOrderDto_NullOrderItems_ShouldMapWithEmptyList() {
            User user = Instancio.of(User.class)
                    .withSeed(10)
                    .create();

            Order order = new Order(
                    1L,
                    user,
                    OrderStatus.PENDING,
                    null,
                    new BigDecimal("5.00"),
                    null,
                    null,
                    LocalDateTime.now()
            );

            OrderDto dto = OrderMapper.fromOrderToOrderDto(order);

            assertNotNull(dto);
            assertTrue(dto.orderItems().isEmpty());
        }

        @Test
        @DisplayName("Debería mapear correctamente con estado PENDING")
        void fromOrderToOrderDto_PendingStatus_ShouldUseProductData() {
            User user = Instancio.of(User.class)
                    .withSeed(10)
                    .create();

            Product product = Instancio.of(Product.class)
                    .ignore(Select.field(Product.class, "image"))
                    .set(Select.field(Product.class, "name"), "Current Product Name")
                    .set(Select.field(Product.class, "basePrice"), new BigDecimal("50.00"))
                    .set(Select.field(Product.class, "discountPercentage"), new BigDecimal("5.00"))
                    .withSeed(30)
                    .create();

            OrderItem orderItem = new OrderItem(
                    1L,
                    product,
                    "Snapshot Name",
                    "image".getBytes(),
                    "test.jpg",
                    2L,
                    new BigDecimal("100.00"),
                    new BigDecimal("10.00")
            );

            List<OrderItem> orderItems = List.of(orderItem);

            Order order = new Order(
                    1L,
                    user,
                    OrderStatus.PENDING,
                    orderItems,
                    null,
                    null,
                    null,
                    LocalDateTime.now()
            );

            OrderDto dto = OrderMapper.fromOrderToOrderDto(order);

            assertNotNull(dto);
            assertEquals(OrderStatus.PENDING, dto.orderStatus());
            assertEquals("Current Product Name", dto.orderItems().get(0).productName());
        }

        @Test
        @DisplayName("Debería mapear correctamente con estado PAYED")
        void fromOrderToOrderDto_PayedStatus_ShouldUseSnapshotData() {
            User user = Instancio.of(User.class)
                    .withSeed(10)
                    .create();

            Product product = Instancio.of(Product.class)
                    .ignore(Select.field(Product.class, "image"))
                    .set(Select.field(Product.class, "name"), "Current Product Name")
                    .set(Select.field(Product.class, "basePrice"), new BigDecimal("50.00"))
                    .set(Select.field(Product.class, "discountPercentage"), new BigDecimal("5.00"))
                    .withSeed(30)
                    .create();

            OrderItem orderItem = new OrderItem(
                    1L,
                    product,
                    "Snapshot Name",
                    "image".getBytes(),
                    "test.jpg",
                    2L,
                    new BigDecimal("100.00"),
                    new BigDecimal("10.00")
            );

            List<OrderItem> orderItems = List.of(orderItem);

            Order order = new Order(
                    1L,
                    user,
                    OrderStatus.PAYED,
                    orderItems,
                    new BigDecimal("5.00"),
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now().minusDays(1)
            );

            OrderDto dto = OrderMapper.fromOrderToOrderDto(order);

            assertNotNull(dto);
            assertEquals(OrderStatus.PAYED, dto.orderStatus());
            assertEquals("Snapshot Name", dto.orderItems().get(0).productName());
        }
    }
}
