package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import com.tagme.tagme_store_back.annotation.DaoTest;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.persistence.dao.jpa.OrderJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.UserJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.ShippingInfoJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.UserJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DaoTest
class OrderJpaDaoImplTest extends BaseJpaDaoTest<OrderJpaDao> {

    @Autowired
    private UserJpaDao userJpaDao;

    @Nested
    class FindAllTests {
        @DisplayName("Given valid page and size, when findAll is called, then return the expected page")
        @Test
        void findAllValidPagination() {
            int page = 1;
            int size = 10;

            var result = dao.findAll(page, size);

            assertNotNull(result);
        }

        @DisplayName("Given page 0, when findAll is called, then it is treated as first page")
        @Test
        void findAllPageZeroTreatedAsFirst() {
            var pageZero = dao.findAll(0, 10);
            var firstPage = dao.findAll(1, 10);

            assertEquals(firstPage.size(), pageZero.size());
        }

        @DisplayName("Given size 0, when findAll is called with size 0, then return list with one element")
        @Test
        void findAllSizeZeroReturnsOneElement() {
            var result = dao.findAll(1, 0);
            // Size 0 is corrected to 1 in implementation
            assertTrue(result.size() <= 1);
        }
    }

    @Nested
    class FindByIdTests {
        @DisplayName("Given an existing order ID, when findById is called, then the corresponding order is returned")
        @Test
        void findByExistingId() {
            // Primero creamos un pedido
            UserJpaEntity user = userJpaDao.findById(1L).get();
            OrderJpaEntity order = new OrderJpaEntity();
            order.setUser(user);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setTotalPrice(BigDecimal.ZERO);
            order.setShippingCost(BigDecimal.ZERO);

            OrderJpaEntity saved = dao.insert(order);

            Optional<OrderJpaEntity> result = dao.findById(saved.getId());
            assertTrue(result.isPresent());
            assertEquals(saved.getId(), result.get().getId());
        }

        @DisplayName("Given a non-existing order ID, when findById is called, then an empty Optional is returned")
        @Test
        void findByNonExistingId() {
            assertTrue(dao.findById(999999L).isEmpty());
        }

        @DisplayName("Given null ID, when findById is called, then an empty Optional is returned")
        @Test
        void findByNullId() {
            assertTrue(dao.findById(null).isEmpty());
        }
    }

    @Nested
    class InsertTests {
        @DisplayName("Given a new order entity, when insert is called, then the order is persisted")
        @Test
        void insertNewOrder() {
            UserJpaEntity user = userJpaDao.findById(1L).get();
            OrderJpaEntity order = new OrderJpaEntity();
            order.setUser(user);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setTotalPrice(BigDecimal.valueOf(100));
            order.setShippingCost(BigDecimal.valueOf(5));

            OrderJpaEntity inserted = dao.insert(order);

            assertNotNull(inserted.getId());
            assertEquals(OrderStatus.PENDING, inserted.getOrderStatus());
        }

        @DisplayName("Given null entity, when insert is called, then throw IllegalArgumentException")
        @Test
        void insertNullEntity() {
            assertThrows(IllegalArgumentException.class, () -> dao.insert(null));
        }

        @DisplayName("Given order without user, when insert is called, then throw IllegalArgumentException")
        @Test
        void insertOrderWithoutUser() {
            OrderJpaEntity order = new OrderJpaEntity();
            order.setOrderStatus(OrderStatus.PENDING);

            assertThrows(IllegalArgumentException.class, () -> dao.insert(order));
        }

        @DisplayName("Given order without status, when insert is called, then throw IllegalArgumentException")
        @Test
        void insertOrderWithoutStatus() {
            UserJpaEntity user = userJpaDao.findById(1L).get();
            OrderJpaEntity order = new OrderJpaEntity();
            order.setUser(user);

            assertThrows(IllegalArgumentException.class, () -> dao.insert(order));
        }
    }

    @Nested
    class UpdateTests {
        @DisplayName("Given an existing order, when update is called, then the order is updated")
        @Test
        void updateExistingOrder() {
            UserJpaEntity user = userJpaDao.findById(1L).get();
            OrderJpaEntity order = new OrderJpaEntity();
            order.setUser(user);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setTotalPrice(BigDecimal.valueOf(100));
            order.setShippingCost(BigDecimal.valueOf(5));

            OrderJpaEntity saved = dao.insert(order);

            saved.setOrderStatus(OrderStatus.PROCESSING);
            saved.setTotalPrice(BigDecimal.valueOf(200));

            OrderJpaEntity updated = dao.update(saved);

            assertEquals(OrderStatus.PROCESSING, updated.getOrderStatus());
            assertEquals(0, BigDecimal.valueOf(200).compareTo(updated.getTotalPrice()));
        }

        @DisplayName("Given null entity, when update is called, then throw IllegalArgumentException")
        @Test
        void updateNullEntity() {
            assertThrows(IllegalArgumentException.class, () -> dao.update(null));
        }

        @DisplayName("Given order without ID, when update is called, then throw IllegalArgumentException")
        @Test
        void updateOrderWithoutId() {
            OrderJpaEntity order = new OrderJpaEntity();
            order.setOrderStatus(OrderStatus.PENDING);

            assertThrows(IllegalArgumentException.class, () -> dao.update(order));
        }

        @DisplayName("Given non-existing order, when update is called, then throw ResourceNotFoundException")
        @Test
        void updateNonExistingOrder() {
            UserJpaEntity user = userJpaDao.findById(1L).get();
            OrderJpaEntity order = new OrderJpaEntity();
            order.setId(999999L);
            order.setUser(user);
            order.setOrderStatus(OrderStatus.PENDING);

            assertThrows(ResourceNotFoundException.class, () -> dao.update(order));
        }

        @DisplayName("Given order with new ShippingInfo, when update is called, then ShippingInfo is added")
        @Test
        void updateOrderWithNewShippingInfo() {
            UserJpaEntity user = userJpaDao.findById(1L).get();
            OrderJpaEntity order = new OrderJpaEntity();
            order.setUser(user);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setTotalPrice(BigDecimal.valueOf(100));
            order.setShippingCost(BigDecimal.valueOf(5));

            OrderJpaEntity saved = dao.insert(order);

            // Add new shipping info
            ShippingInfoJpaEntity shippingInfo = new ShippingInfoJpaEntity();
            shippingInfo.setFirstName("John");
            shippingInfo.setLastName("Doe");
            shippingInfo.setEmail("john@example.com");
            shippingInfo.setAddress("123 Test St");
            shippingInfo.setCity("Test City");
            shippingInfo.setPostalCode("12345");
            shippingInfo.setCountry("Spain");
            saved.setShippingInfo(shippingInfo);

            OrderJpaEntity updated = dao.update(saved);

            assertNotNull(updated.getShippingInfo());
            assertEquals("John", updated.getShippingInfo().getFirstName());
        }

        @DisplayName("Given order with existing ShippingInfo, when update is called, then ShippingInfo is updated")
        @Test
        void updateOrderWithExistingShippingInfo() {
            UserJpaEntity user = userJpaDao.findById(1L).get();
            OrderJpaEntity order = new OrderJpaEntity();
            order.setUser(user);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setTotalPrice(BigDecimal.valueOf(100));
            order.setShippingCost(BigDecimal.valueOf(5));

            // Add initial shipping info
            ShippingInfoJpaEntity shippingInfo = new ShippingInfoJpaEntity();
            shippingInfo.setFirstName("John");
            shippingInfo.setLastName("Doe");
            shippingInfo.setEmail("john@example.com");
            shippingInfo.setAddress("123 Test St");
            shippingInfo.setCity("Test City");
            shippingInfo.setPostalCode("12345");
            shippingInfo.setCountry("Spain");
            order.setShippingInfo(shippingInfo);

            OrderJpaEntity saved = dao.insert(order);

            // Update shipping info
            ShippingInfoJpaEntity newShippingInfo = new ShippingInfoJpaEntity();
            newShippingInfo.setFirstName("Jane");
            newShippingInfo.setLastName("Smith");
            newShippingInfo.setEmail("jane@example.com");
            newShippingInfo.setAddress("456 New St");
            newShippingInfo.setCity("New City");
            newShippingInfo.setPostalCode("67890");
            newShippingInfo.setCountry("France");
            saved.setShippingInfo(newShippingInfo);

            OrderJpaEntity updated = dao.update(saved);

            assertEquals("Jane", updated.getShippingInfo().getFirstName());
            assertEquals("New City", updated.getShippingInfo().getCity());
        }

        @DisplayName("Given order with ShippingInfo, when update removes ShippingInfo, then it is removed")
        @Test
        void updateOrderRemoveShippingInfo() {
            UserJpaEntity user = userJpaDao.findById(1L).get();
            OrderJpaEntity order = new OrderJpaEntity();
            order.setUser(user);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setTotalPrice(BigDecimal.valueOf(100));
            order.setShippingCost(BigDecimal.valueOf(5));

            OrderJpaEntity saved = dao.insert(order);

            // Set shipping info to null
            saved.setShippingInfo(null);

            OrderJpaEntity updated = dao.update(saved);

            assertNull(updated.getShippingInfo());
        }
    }

    @Nested
    class DeleteTests {
        @DisplayName("Given an existing order, when deleteById is called, then the order is removed")
        @Test
        void deleteExistingOrder() {
            UserJpaEntity user = userJpaDao.findById(1L).get();
            OrderJpaEntity order = new OrderJpaEntity();
            order.setUser(user);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setTotalPrice(BigDecimal.ZERO);
            order.setShippingCost(BigDecimal.ZERO);

            OrderJpaEntity saved = dao.insert(order);
            Long id = saved.getId();

            dao.deleteById(id);

            assertTrue(dao.findById(id).isEmpty());
        }

        @DisplayName("Given null ID, when deleteById is called, then throw IllegalArgumentException")
        @Test
        void deleteNullId() {
            assertThrows(IllegalArgumentException.class, () -> dao.deleteById(null));
        }

        @DisplayName("Given non-existing ID, when deleteById is called, then throw ResourceNotFoundException")
        @Test
        void deleteNonExistingId() {
            assertThrows(ResourceNotFoundException.class, () -> dao.deleteById(999999L));
        }
    }

    @Nested
    class CountTests {
        @DisplayName("When count is called, then return number of orders")
        @Test
        void countReturnsTotal() {
            Long count = dao.count();
            assertNotNull(count);
            assertTrue(count >= 0);
        }
    }

    @Nested
    class FindByUserIdTests {
        @DisplayName("Given existing user, when findByUserId is called, then return user's orders")
        @Test
        void findByExistingUserId() {
            UserJpaEntity user = userJpaDao.findById(1L).get();

            // Crear un pedido para el usuario
            OrderJpaEntity order = new OrderJpaEntity();
            order.setUser(user);
            order.setOrderStatus(OrderStatus.PAYED);
            order.setTotalPrice(BigDecimal.valueOf(50));
            order.setShippingCost(BigDecimal.valueOf(5));
            dao.insert(order);

            List<OrderJpaEntity> orders = dao.findByUserId(1L);

            assertNotNull(orders);
        }

        @DisplayName("Given null userId, when findByUserId is called, then throw IllegalArgumentException")
        @Test
        void findByNullUserId() {
            assertThrows(IllegalArgumentException.class, () -> dao.findByUserId(null));
        }
    }

    @Nested
    class FindActiveOrderTests {
        @DisplayName("Given user with active order, when findActiveOrderByUserId is called, then return the order")
        @Test
        void findActiveOrderByUserId() {
            UserJpaEntity user = userJpaDao.findById(1L).get();

            // Primero eliminar pedidos PENDING existentes si los hay
            List<OrderJpaEntity> existingOrders = dao.findByUserId(1L);
            for (OrderJpaEntity o : existingOrders) {
                if (o.getOrderStatus() == OrderStatus.PENDING) {
                    dao.deleteById(o.getId());
                }
            }

            // Crear pedido PENDING
            OrderJpaEntity order = new OrderJpaEntity();
            order.setUser(user);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setTotalPrice(BigDecimal.ZERO);
            order.setShippingCost(BigDecimal.ZERO);
            dao.insert(order);

            Optional<OrderJpaEntity> result = dao.findActiveOrderByUserId(1L);

            assertTrue(result.isPresent());
            assertEquals(OrderStatus.PENDING, result.get().getOrderStatus());
        }

        @DisplayName("Given null userId, when findActiveOrderByUserId is called, then return empty")
        @Test
        void findActiveOrderByNullUserId() {
            assertTrue(dao.findActiveOrderByUserId(null).isEmpty());
        }
    }

    @Nested
    class GetOrderStatusTests {
        @DisplayName("Given existing order, when getOrderStatus is called, then return the status")
        @Test
        void getOrderStatusExisting() {
            UserJpaEntity user = userJpaDao.findById(1L).get();
            OrderJpaEntity order = new OrderJpaEntity();
            order.setUser(user);
            order.setOrderStatus(OrderStatus.PROCESSING);
            order.setTotalPrice(BigDecimal.ZERO);
            order.setShippingCost(BigDecimal.ZERO);

            OrderJpaEntity saved = dao.insert(order);

            Optional<OrderStatus> status = dao.getOrderStatus(saved.getId());

            assertTrue(status.isPresent());
            assertEquals(OrderStatus.PROCESSING, status.get());
        }

        @DisplayName("Given non-existing order, when getOrderStatus is called, then return empty")
        @Test
        void getOrderStatusNonExisting() {
            assertTrue(dao.getOrderStatus(999999L).isEmpty());
        }

        @DisplayName("Given null ID, when getOrderStatus is called, then return empty")
        @Test
        void getOrderStatusNullId() {
            assertTrue(dao.getOrderStatus(null).isEmpty());
        }
    }

    @Nested
    class FindNonActiveOrdersTests {
        @DisplayName("Given user with non-active orders, when findNonActiveOrdersByUserId is called, then return orders")
        @Test
        void findNonActiveOrdersByUserId() {
            UserJpaEntity user = userJpaDao.findById(1L).get();

            // Crear pedido PAYED
            OrderJpaEntity order = new OrderJpaEntity();
            order.setUser(user);
            order.setOrderStatus(OrderStatus.PAYED);
            order.setTotalPrice(BigDecimal.valueOf(100));
            order.setShippingCost(BigDecimal.valueOf(10));
            dao.insert(order);

            List<OrderJpaEntity> orders = dao.findNonActiveOrdersByUserId(1L);

            assertNotNull(orders);
            assertTrue(orders.stream().noneMatch(o -> o.getOrderStatus() == OrderStatus.PENDING));
        }

        @DisplayName("Given null userId, when findNonActiveOrdersByUserId is called, then throw IllegalArgumentException")
        @Test
        void findNonActiveOrdersByNullUserId() {
            assertThrows(IllegalArgumentException.class, () -> dao.findNonActiveOrdersByUserId(null));
        }
    }

    @Nested
    class FindAllWithFiltersTests {
        @DisplayName("When findAllWithFilters is called with no filters, then return all orders")
        @Test
        void findAllWithNoFilters() {
            List<OrderJpaEntity> result = dao.findAllWithFilters(1, 10, null, null);
            assertNotNull(result);
        }

        @DisplayName("When findAllWithFilters is called with status filter, then return filtered orders")
        @Test
        void findAllWithStatusFilter() {
            List<OrderJpaEntity> result = dao.findAllWithFilters(1, 10, OrderStatus.PAYED, null);
            assertNotNull(result);
            assertTrue(result.stream().allMatch(o -> o.getOrderStatus() == OrderStatus.PAYED));
        }

        @DisplayName("When findAllWithFilters is called with userId filter, then return filtered orders")
        @Test
        void findAllWithUserIdFilter() {
            List<OrderJpaEntity> result = dao.findAllWithFilters(1, 10, null, 1L);
            assertNotNull(result);
            assertTrue(result.stream().allMatch(o -> o.getUser().getId().equals(1L)));
        }
    }

    @Nested
    class CountWithFiltersTests {
        @DisplayName("When countWithFilters is called with no filters, then return total count")
        @Test
        void countWithNoFilters() {
            Long count = dao.countWithFilters(null, null);
            assertNotNull(count);
            assertTrue(count >= 0);
        }

        @DisplayName("When countWithFilters is called with filters, then return filtered count")
        @Test
        void countWithFilters() {
            Long count = dao.countWithFilters(OrderStatus.PAYED, null);
            assertNotNull(count);
            assertTrue(count >= 0);
        }
    }
}
