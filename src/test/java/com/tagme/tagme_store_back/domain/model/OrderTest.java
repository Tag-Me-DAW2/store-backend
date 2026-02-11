package com.tagme.tagme_store_back.domain.model;

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

@DisplayName("Order Model Tests")
class OrderTest {

    @Nested
    @DisplayName("calculateTotalPrice Tests")
    class CalculateTotalPriceTests {

        @Test
        @DisplayName("Debería calcular precio total con items")
        void order_WithItems_CalculatesTotalPrice() {
            User user = Instancio.of(User.class).withSeed(10).create();

            OrderItem item1 = new OrderItem(
                    1L, null, "Product 1", null, "img1.jpg",
                    2L, new BigDecimal("10.00"), new BigDecimal("0.00")
            );
            OrderItem item2 = new OrderItem(
                    2L, null, "Product 2", null, "img2.jpg",
                    1L, new BigDecimal("50.00"), new BigDecimal("10.00")
            );

            List<OrderItem> items = List.of(item1, item2);

            Order order = new Order(
                    1L, user, OrderStatus.PENDING, items,
                    null, null, null, LocalDateTime.now()
            );

            // item1: 10 * 2 = 20, item2: 50 - 5 (10%) = 45
            BigDecimal expected = new BigDecimal("20.00").add(new BigDecimal("45.00"));
            assertEquals(expected, order.getTotalPrice());
        }

        @Test
        @DisplayName("Debería devolver cero con lista vacía")
        void order_EmptyItems_ReturnsZero() {
            User user = Instancio.of(User.class).withSeed(10).create();

            Order order = new Order(
                    1L, user, OrderStatus.PENDING, new ArrayList<>(),
                    null, null, null, LocalDateTime.now()
            );

            assertEquals(BigDecimal.ZERO, order.getTotalPrice());
        }

        @Test
        @DisplayName("Debería devolver cero con items null")
        void order_NullItems_ReturnsZero() {
            User user = Instancio.of(User.class).withSeed(10).create();

            Order order = new Order(
                    1L, user, OrderStatus.PENDING, null,
                    null, null, null, LocalDateTime.now()
            );

            assertEquals(BigDecimal.ZERO, order.getTotalPrice());
        }
    }

    @Nested
    @DisplayName("calculateShippingCost Tests")
    class CalculateShippingCostTests {

        @Test
        @DisplayName("Debería cobrar envío cuando total es menor a 50")
        void calculateShippingCost_Under50_Returns5() {
            User user = Instancio.of(User.class).withSeed(10).create();

            OrderItem item = new OrderItem(
                    1L, null, "Product", null, "img.jpg",
                    1L, new BigDecimal("30.00"), new BigDecimal("0.00")
            );

            Order order = new Order(
                    1L, user, OrderStatus.PENDING, List.of(item),
                    null, null, null, LocalDateTime.now()
            );

            assertEquals(new BigDecimal("5.00"), order.calculateShippingCost());
        }

        @Test
        @DisplayName("Debería ser gratis cuando total es 50 o más")
        void calculateShippingCost_50OrMore_ReturnsZero() {
            User user = Instancio.of(User.class).withSeed(10).create();

            OrderItem item = new OrderItem(
                    1L, null, "Product", null, "img.jpg",
                    1L, new BigDecimal("50.00"), new BigDecimal("0.00")
            );

            Order order = new Order(
                    1L, user, OrderStatus.PENDING, List.of(item),
                    null, null, null, LocalDateTime.now()
            );

            assertEquals(BigDecimal.ZERO, order.calculateShippingCost());
        }

        @Test
        @DisplayName("Debería ser gratis cuando total es mayor a 50")
        void calculateShippingCost_Over50_ReturnsZero() {
            User user = Instancio.of(User.class).withSeed(10).create();

            OrderItem item = new OrderItem(
                    1L, null, "Product", null, "img.jpg",
                    1L, new BigDecimal("100.00"), new BigDecimal("0.00")
            );

            Order order = new Order(
                    1L, user, OrderStatus.PENDING, List.of(item),
                    null, null, null, LocalDateTime.now()
            );

            assertEquals(BigDecimal.ZERO, order.calculateShippingCost());
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Debería usar shipping cost proporcionado si no es null")
        void order_WithProvidedShippingCost_UsesProvided() {
            User user = Instancio.of(User.class).withSeed(10).create();
            BigDecimal customShippingCost = new BigDecimal("10.00");

            Order order = new Order(
                    1L, user, OrderStatus.PENDING, new ArrayList<>(),
                    customShippingCost, null, null, LocalDateTime.now()
            );

            assertEquals(customShippingCost, order.getShippingCost());
        }

        @Test
        @DisplayName("Debería calcular shipping cost si es null")
        void order_WithNullShippingCost_CalculatesIt() {
            User user = Instancio.of(User.class).withSeed(10).create();

            Order order = new Order(
                    1L, user, OrderStatus.PENDING, new ArrayList<>(),
                    null, null, null, LocalDateTime.now()
            );

            // Total es 0, por lo que debería cobrar envío
            assertEquals(new BigDecimal("5.00"), order.getShippingCost());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Debería ser igual a sí mismo")
        void equals_SameObject_ReturnsTrue() {
            User user = Instancio.of(User.class).withSeed(10).create();
            Order order = new Order(
                    1L, user, OrderStatus.PENDING, new ArrayList<>(),
                    null, null, null, LocalDateTime.now()
            );

            assertEquals(order, order);
        }

        @Test
        @DisplayName("Debería ser diferente de null")
        void equals_Null_ReturnsFalse() {
            User user = Instancio.of(User.class).withSeed(10).create();
            Order order = new Order(
                    1L, user, OrderStatus.PENDING, new ArrayList<>(),
                    null, null, null, LocalDateTime.now()
            );

            assertNotEquals(null, order);
        }

        @Test
        @DisplayName("Debería ser diferente de otra clase")
        void equals_DifferentClass_ReturnsFalse() {
            User user = Instancio.of(User.class).withSeed(10).create();
            Order order = new Order(
                    1L, user, OrderStatus.PENDING, new ArrayList<>(),
                    null, null, null, LocalDateTime.now()
            );

            assertNotEquals("string", order);
        }

        @Test
        @DisplayName("HashCode debería ser consistente")
        void hashCode_SameValues_SameHash() {
            User user = Instancio.of(User.class).withSeed(10).create();
            LocalDateTime now = LocalDateTime.now();

            Order order1 = new Order(
                    1L, user, OrderStatus.PENDING, new ArrayList<>(),
                    new BigDecimal("5.00"), null, null, now
            );
            Order order2 = new Order(
                    1L, user, OrderStatus.PENDING, new ArrayList<>(),
                    new BigDecimal("5.00"), null, null, now
            );

            assertEquals(order1.hashCode(), order2.hashCode());
        }
    }
}
