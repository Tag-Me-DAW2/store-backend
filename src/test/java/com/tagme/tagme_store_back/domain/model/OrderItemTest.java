package com.tagme.tagme_store_back.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderItem Model Tests")
class OrderItemTest {

    @Nested
    @DisplayName("calculateFinalPrice Tests")
    class CalculateFinalPriceTests {

        @Test
        @DisplayName("Debería calcular precio sin descuento")
        void orderItem_NoDiscount_CalculatesCorrectly() {
            OrderItem item = new OrderItem(
                    1L, null, "Product", null, "img.jpg",
                    2L, new BigDecimal("100.00"), new BigDecimal("0.00")
            );

            // 100 * 2 = 200
            assertEquals(new BigDecimal("200.00"), item.getTotal());
        }

        @Test
        @DisplayName("Debería calcular precio con descuento del 10%")
        void orderItem_10PercentDiscount_CalculatesCorrectly() {
            OrderItem item = new OrderItem(
                    1L, null, "Product", null, "img.jpg",
                    1L, new BigDecimal("100.00"), new BigDecimal("10.00")
            );

            // 100 - 10% = 90
            assertEquals(new BigDecimal("90.00"), item.getTotal());
        }

        @Test
        @DisplayName("Debería calcular precio con descuento del 50%")
        void orderItem_50PercentDiscount_CalculatesCorrectly() {
            OrderItem item = new OrderItem(
                    1L, null, "Product", null, "img.jpg",
                    3L, new BigDecimal("50.00"), new BigDecimal("50.00")
            );

            // (50 - 25) * 3 = 75
            assertEquals(new BigDecimal("75.00"), item.getTotal());
        }

        @Test
        @DisplayName("Debería calcular precio con descuento del 100%")
        void orderItem_100PercentDiscount_ReturnsZero() {
            OrderItem item = new OrderItem(
                    1L, null, "Product", null, "img.jpg",
                    2L, new BigDecimal("100.00"), new BigDecimal("100.00")
            );

            assertEquals(new BigDecimal("0.00"), item.getTotal());
        }

        @Test
        @DisplayName("Debería manejar precios con decimales")
        void orderItem_DecimalPrice_CalculatesCorrectly() {
            OrderItem item = new OrderItem(
                    1L, null, "Product", null, "img.jpg",
                    1L, new BigDecimal("19.99"), new BigDecimal("15.00")
            );

            // 19.99 - 15% = 19.99 - 2.9985 = 16.9915 ≈ 16.99
            assertEquals(new BigDecimal("16.99"), item.getTotal());
        }

        @Test
        @DisplayName("Debería manejar cantidad grande")
        void orderItem_LargeQuantity_CalculatesCorrectly() {
            OrderItem item = new OrderItem(
                    1L, null, "Product", null, "img.jpg",
                    100L, new BigDecimal("10.00"), new BigDecimal("5.00")
            );

            // (10 - 0.5) * 100 = 950
            assertEquals(new BigDecimal("950.00"), item.getTotal());
        }
    }

    @Nested
    @DisplayName("Getters and Setters Tests")
    class GettersSettersTests {

        @Test
        @DisplayName("Debería establecer y obtener todos los campos")
        void orderItem_SettersAndGetters_WorkCorrectly() {
            OrderItem item = new OrderItem(
                    1L, null, "Product", null, "img.jpg",
                    1L, new BigDecimal("10.00"), new BigDecimal("0.00")
            );

            item.setId(2L);
            item.setProductName("New Product");
            item.setProductImageName("new.jpg");
            item.setQuantity(5L);
            item.setBasePrice(new BigDecimal("20.00"));
            item.setDiscountPercentage(new BigDecimal("10.00"));

            assertAll(
                    () -> assertEquals(2L, item.getId()),
                    () -> assertEquals("New Product", item.getProductName()),
                    () -> assertEquals("new.jpg", item.getProductImageName()),
                    () -> assertEquals(5L, item.getQuantity()),
                    () -> assertEquals(new BigDecimal("20.00"), item.getBasePrice()),
                    () -> assertEquals(new BigDecimal("10.00"), item.getDiscountPercentage())
            );
        }

        @Test
        @DisplayName("Debería manejar imagen como byte array")
        void orderItem_ImageByteArray_HandledCorrectly() {
            byte[] image = "test image data".getBytes();

            OrderItem item = new OrderItem(
                    1L, null, "Product", image, "img.jpg",
                    1L, new BigDecimal("10.00"), new BigDecimal("0.00")
            );

            assertArrayEquals(image, item.getProductImage());

            byte[] newImage = "new image".getBytes();
            item.setProductImage(newImage);
            assertArrayEquals(newImage, item.getProductImage());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Debería ser igual a sí mismo")
        void equals_SameObject_ReturnsTrue() {
            OrderItem item = new OrderItem(
                    1L, null, "Product", null, "img.jpg",
                    1L, new BigDecimal("10.00"), new BigDecimal("0.00")
            );

            assertEquals(item, item);
        }

        @Test
        @DisplayName("Debería ser diferente de null")
        void equals_Null_ReturnsFalse() {
            OrderItem item = new OrderItem(
                    1L, null, "Product", null, "img.jpg",
                    1L, new BigDecimal("10.00"), new BigDecimal("0.00")
            );

            assertNotEquals(null, item);
        }
    }
}
