package com.tagme.tagme_store_back.domain.mapper;

import com.tagme.tagme_store_back.domain.dto.OrderItemDto;
import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.model.OrderItem;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.model.Product;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.sql.rowset.serial.SerialBlob;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderItemMapper Tests")
class OrderItemMapperTest {

    @Nested
    @DisplayName("fromOrderItemDtoToOrderItem Tests")
    class FromOrderItemDtoToOrderItemTests {
        @Test
        @DisplayName("Debería devolver null cuando el DTO es null")
        void fromOrderItemDtoToOrderItem_NullInput_ReturnsNull() {
            assertNull(OrderItemMapper.fromOrderItemDtoToOrderItem(null));
        }

        @Test
        @DisplayName("Debería mapear correctamente DTO a modelo")
        void fromOrderItemDtoToOrderItem_ShouldMapCorrectly() {
            ProductDto productDto = Instancio.of(ProductDto.class)
                    .ignore(Select.field(ProductDto.class, "image"))
                    .withSeed(10)
                    .create();
            
            OrderItemDto dto = new OrderItemDto(
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

            OrderItem orderItem = OrderItemMapper.fromOrderItemDtoToOrderItem(dto);

            assertNotNull(orderItem);
            assertAll(
                    () -> assertEquals(dto.id(), orderItem.getId()),
                    () -> assertEquals(dto.productName(), orderItem.getProductName()),
                    () -> assertEquals(dto.productImageName(), orderItem.getProductImageName()),
                    () -> assertEquals(dto.quantity(), orderItem.getQuantity()),
                    () -> assertEquals(dto.basePrice(), orderItem.getBasePrice()),
                    () -> assertEquals(dto.discountPercentage(), orderItem.getDiscountPercentage())
            );
        }

        @Test
        @DisplayName("Debería mapear correctamente con imagen blob")
        void fromOrderItemDtoToOrderItem_WithBlobImage_ShouldMapCorrectly() throws SQLException {
            ProductDto productDto = Instancio.of(ProductDto.class)
                    .ignore(Select.field(ProductDto.class, "image"))
                    .withSeed(10)
                    .create();
            
            byte[] imageBytes = "test image data".getBytes();
            Blob imageBlob = new SerialBlob(imageBytes);
            
            OrderItemDto dto = new OrderItemDto(
                    1L,
                    productDto,
                    "Test Product",
                    imageBlob,
                    "test.jpg",
                    2L,
                    new BigDecimal("100.00"),
                    new BigDecimal("10.00"),
                    new BigDecimal("180.00")
            );

            OrderItem orderItem = OrderItemMapper.fromOrderItemDtoToOrderItem(dto);

            assertNotNull(orderItem);
            assertNotNull(orderItem.getProductImage());
            assertArrayEquals(imageBytes, orderItem.getProductImage());
        }
    }

    @Nested
    @DisplayName("fromOrderItemToOrderItemDto Tests con OrderStatus")
    class FromOrderItemToOrderItemDtoWithStatusTests {
        @Test
        @DisplayName("Debería devolver null cuando el modelo es null")
        void fromOrderItemToOrderItemDto_NullInput_ReturnsNull() {
            assertNull(OrderItemMapper.fromOrderItemToOrderItemDto(null, OrderStatus.PAYED));
        }

        @Test
        @DisplayName("Debería usar datos del producto cuando el estado es PENDING")
        void fromOrderItemToOrderItemDto_PendingStatus_UsesProductData() {
            Product product = Instancio.of(Product.class)
                    .set(Select.field(Product.class, "name"), "Product Name")
                    .set(Select.field(Product.class, "basePrice"), new BigDecimal("50.00"))
                    .set(Select.field(Product.class, "discountPercentage"), new BigDecimal("5.00"))
                    .ignore(Select.field(Product.class, "image"))
                    .withSeed(10)
                    .create();

            OrderItem orderItem = new OrderItem(
                    1L,
                    product,
                    "Snapshot Name",
                    "snapshot image".getBytes(),
                    "snapshot.jpg",
                    2L,
                    new BigDecimal("100.00"),
                    new BigDecimal("10.00")
            );

            OrderItemDto dto = OrderItemMapper.fromOrderItemToOrderItemDto(orderItem, OrderStatus.PENDING);

            assertNotNull(dto);
            assertEquals(product.getName(), dto.productName());
            assertEquals(product.getBasePrice(), dto.basePrice());
            assertEquals(product.getDiscountPercentage(), dto.discountPercentage());
        }

        @Test
        @DisplayName("Debería usar datos del snapshot cuando el estado es PAYED")
        void fromOrderItemToOrderItemDto_PayedStatus_UsesSnapshotData() {
            Product product = Instancio.of(Product.class)
                    .set(Select.field(Product.class, "name"), "Product Name")
                    .set(Select.field(Product.class, "basePrice"), new BigDecimal("50.00"))
                    .set(Select.field(Product.class, "discountPercentage"), new BigDecimal("5.00"))
                    .ignore(Select.field(Product.class, "image"))
                    .withSeed(10)
                    .create();

            OrderItem orderItem = new OrderItem(
                    1L,
                    product,
                    "Snapshot Name",
                    "snapshot image".getBytes(),
                    "snapshot.jpg",
                    2L,
                    new BigDecimal("100.00"),
                    new BigDecimal("10.00")
            );

            OrderItemDto dto = OrderItemMapper.fromOrderItemToOrderItemDto(orderItem, OrderStatus.PAYED);

            assertNotNull(dto);
            assertEquals("Snapshot Name", dto.productName());
            assertEquals(new BigDecimal("100.00"), dto.basePrice());
            assertEquals(new BigDecimal("10.00"), dto.discountPercentage());
        }

        @Test
        @DisplayName("Debería usar datos del snapshot cuando el estado es PROCESSING")
        void fromOrderItemToOrderItemDto_ProcessingStatus_UsesSnapshotData() {
            Product product = Instancio.of(Product.class)
                    .set(Select.field(Product.class, "name"), "Product Name")
                    .ignore(Select.field(Product.class, "image"))
                    .withSeed(10)
                    .create();

            OrderItem orderItem = new OrderItem(
                    1L,
                    product,
                    "Snapshot Name",
                    "snapshot image".getBytes(),
                    "snapshot.jpg",
                    2L,
                    new BigDecimal("100.00"),
                    new BigDecimal("10.00")
            );

            OrderItemDto dto = OrderItemMapper.fromOrderItemToOrderItemDto(orderItem, OrderStatus.PROCESSING);

            assertNotNull(dto);
            assertEquals("Snapshot Name", dto.productName());
        }

        @Test
        @DisplayName("Debería usar snapshot cuando el producto es null y estado es PENDING")
        void fromOrderItemToOrderItemDto_PendingStatusNullProduct_UsesSnapshotData() {
            OrderItem orderItem = new OrderItem(
                    1L,
                    null,
                    "Snapshot Name",
                    "snapshot image".getBytes(),
                    "snapshot.jpg",
                    2L,
                    new BigDecimal("100.00"),
                    new BigDecimal("10.00")
            );

            OrderItemDto dto = OrderItemMapper.fromOrderItemToOrderItemDto(orderItem, OrderStatus.PENDING);

            assertNotNull(dto);
            assertEquals("Snapshot Name", dto.productName());
        }
    }

    @Nested
    @DisplayName("fromOrderItemToOrderItemDto Tests sin OrderStatus")
    class FromOrderItemToOrderItemDtoWithoutStatusTests {
        @Test
        @DisplayName("Debería usar PAYED como estado por defecto")
        void fromOrderItemToOrderItemDto_NoStatus_UsesPayed() {
            Product product = Instancio.of(Product.class)
                    .set(Select.field(Product.class, "name"), "Product Name")
                    .ignore(Select.field(Product.class, "image"))
                    .withSeed(10)
                    .create();

            OrderItem orderItem = new OrderItem(
                    1L,
                    product,
                    "Snapshot Name",
                    "snapshot image".getBytes(),
                    "snapshot.jpg",
                    2L,
                    new BigDecimal("100.00"),
                    new BigDecimal("10.00")
            );

            OrderItemDto dto = OrderItemMapper.fromOrderItemToOrderItemDto(orderItem);

            assertNotNull(dto);
            assertEquals("Snapshot Name", dto.productName());
        }
    }
}
