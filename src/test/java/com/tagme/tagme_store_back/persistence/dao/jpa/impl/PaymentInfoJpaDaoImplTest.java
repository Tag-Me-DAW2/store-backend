package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import com.tagme.tagme_store_back.annotation.DaoTest;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.persistence.dao.jpa.OrderJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.PaymentInfoJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.UserJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.PaymentInfoJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.UserJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DaoTest
class PaymentInfoJpaDaoImplTest extends BaseJpaDaoTest<PaymentInfoJpaDao> {

    @Autowired
    private UserJpaDao userJpaDao;

    @Autowired
    private OrderJpaDao orderJpaDao;

    private OrderJpaEntity createTestOrder() {
        UserJpaEntity user = userJpaDao.findById(1L).get();
        OrderJpaEntity order = new OrderJpaEntity();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setTotalPrice(BigDecimal.valueOf(100));
        order.setShippingCost(BigDecimal.valueOf(5));
        return orderJpaDao.insert(order);
    }

    @Nested
    class SaveTests {
        @DisplayName("Given a new payment info, when save is called, then it is persisted")
        @Test
        void saveNewPaymentInfo() {
            OrderJpaEntity order = createTestOrder();

            PaymentInfoJpaEntity paymentInfo = new PaymentInfoJpaEntity();
            paymentInfo.setOrder(order);
            paymentInfo.setCardNumber("4111111111111111");
            paymentInfo.setCardHolderName("Test User");
            paymentInfo.setCvv("123");
            paymentInfo.setExpirationDate("12/30");

            PaymentInfoJpaEntity saved = dao.save(paymentInfo);

            assertNotNull(saved.getId());
            assertEquals("4111111111111111", saved.getCardNumber());
        }

        @DisplayName("Given null entity, when save is called, then throw IllegalArgumentException")
        @Test
        void saveNullEntity() {
            assertThrows(IllegalArgumentException.class, () -> dao.save(null));
        }

        @DisplayName("Given existing payment info, when save is called, then it is updated")
        @Test
        void updateExistingPaymentInfo() {
            OrderJpaEntity order = createTestOrder();

            PaymentInfoJpaEntity paymentInfo = new PaymentInfoJpaEntity();
            paymentInfo.setOrder(order);
            paymentInfo.setCardNumber("4111111111111111");
            paymentInfo.setCardHolderName("Test User");
            paymentInfo.setCvv("123");
            paymentInfo.setExpirationDate("12/30");

            PaymentInfoJpaEntity saved = dao.save(paymentInfo);

            saved.setCardHolderName("Updated User");
            PaymentInfoJpaEntity updated = dao.save(saved);

            assertEquals("Updated User", updated.getCardHolderName());
        }
    }

    @Nested
    class FindByOrderIdTests {
        @DisplayName("Given existing order with payment info, when findByOrderId is called, then return payment info")
        @Test
        void findByExistingOrderId() {
            OrderJpaEntity order = createTestOrder();

            PaymentInfoJpaEntity paymentInfo = new PaymentInfoJpaEntity();
            paymentInfo.setOrder(order);
            paymentInfo.setCardNumber("4111111111111111");
            paymentInfo.setCardHolderName("Test User");
            paymentInfo.setCvv("123");
            paymentInfo.setExpirationDate("12/30");
            dao.save(paymentInfo);

            Optional<PaymentInfoJpaEntity> result = dao.findByOrderId(order.getId());

            assertTrue(result.isPresent());
            assertEquals(order.getId(), result.get().getOrder().getId());
        }

        @DisplayName("Given non-existing order id, when findByOrderId is called, then return empty")
        @Test
        void findByNonExistingOrderId() {
            Optional<PaymentInfoJpaEntity> result = dao.findByOrderId(999999L);
            assertTrue(result.isEmpty());
        }

        @DisplayName("Given null order id, when findByOrderId is called, then return empty")
        @Test
        void findByNullOrderId() {
            Optional<PaymentInfoJpaEntity> result = dao.findByOrderId(null);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class DeleteByOrderIdTests {
        @DisplayName("Given existing payment info, when deleteByOrderId is called, then it is deleted")
        @Test
        void deleteByExistingOrderId() {
            OrderJpaEntity order = createTestOrder();

            PaymentInfoJpaEntity paymentInfo = new PaymentInfoJpaEntity();
            paymentInfo.setOrder(order);
            paymentInfo.setCardNumber("4111111111111111");
            paymentInfo.setCardHolderName("Test User");
            paymentInfo.setCvv("123");
            paymentInfo.setExpirationDate("12/30");
            dao.save(paymentInfo);

            dao.deleteByOrderId(order.getId());

            assertTrue(dao.findByOrderId(order.getId()).isEmpty());
        }

        @DisplayName("Given null order id, when deleteByOrderId is called, then nothing happens")
        @Test
        void deleteByNullOrderId() {
            // Should not throw
            assertDoesNotThrow(() -> dao.deleteByOrderId(null));
        }

        @DisplayName("Given non-existing order id, when deleteByOrderId is called, then nothing happens")
        @Test
        void deleteByNonExistingOrderId() {
            // Should not throw
            assertDoesNotThrow(() -> dao.deleteByOrderId(999999L));
        }
    }
}
