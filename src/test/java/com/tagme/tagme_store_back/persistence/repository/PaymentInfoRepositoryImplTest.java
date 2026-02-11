package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.domain.dto.PaymentInfoDto;
import com.tagme.tagme_store_back.persistence.dao.jpa.OrderJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.PaymentInfoJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.PaymentInfoJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.UserJpaEntity;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentInfoRepositoryImplTest {

    @Mock
    private PaymentInfoJpaDao paymentInfoJpaDao;

    @Mock
    private OrderJpaDao orderJpaDao;

    @InjectMocks
    private PaymentInfoRepositoryImpl paymentInfoRepository;

    private OrderJpaEntity orderJpaEntity;
    private PaymentInfoJpaEntity paymentInfoJpaEntity;

    @BeforeEach
    void setUp() {
        UserJpaEntity userJpaEntity = Instancio.of(UserJpaEntity.class)
                .set(field(UserJpaEntity::getId), 1L)
                .create();

        orderJpaEntity = new OrderJpaEntity();
        orderJpaEntity.setId(1L);
        orderJpaEntity.setUser(userJpaEntity);

        paymentInfoJpaEntity = new PaymentInfoJpaEntity();
        paymentInfoJpaEntity.setId(1L);
        paymentInfoJpaEntity.setOrder(orderJpaEntity);
        paymentInfoJpaEntity.setCardNumber("4111111111111111");
        paymentInfoJpaEntity.setCardHolderName("Test User");
        paymentInfoJpaEntity.setCvv("123");
        paymentInfoJpaEntity.setExpirationDate("12/30");
    }

    @Nested
    class SaveTests {
        @DisplayName("Given valid paymentInfoDto, when save is called, then return saved PaymentInfoDto")
        @Test
        void saveSuccess() {
            PaymentInfoDto dto = new PaymentInfoDto(
                    null,
                    1L,
                    "4111111111111111",
                    "Test User",
                    "123",
                    "12/30"
            );

            when(orderJpaDao.findById(1L)).thenReturn(Optional.of(orderJpaEntity));
            when(paymentInfoJpaDao.save(any())).thenReturn(paymentInfoJpaEntity);

            PaymentInfoDto result = paymentInfoRepository.save(dto);

            assertNotNull(result);
            assertEquals("4111111111111111", result.cardNumber());
            verify(paymentInfoJpaDao).save(any());
        }

        @DisplayName("Given null paymentInfoDto, when save is called, then throw IllegalArgumentException")
        @Test
        void saveNullDto() {
            assertThrows(IllegalArgumentException.class, () -> paymentInfoRepository.save(null));
        }

        @DisplayName("Given non-existing orderId, when save is called, then throw IllegalArgumentException")
        @Test
        void saveOrderNotFound() {
            PaymentInfoDto dto = new PaymentInfoDto(
                    null,
                    999L,
                    "4111111111111111",
                    "Test User",
                    "123",
                    "12/30"
            );

            when(orderJpaDao.findById(999L)).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () -> paymentInfoRepository.save(dto));
        }
    }

    @Nested
    class FindByOrderIdTests {
        @DisplayName("Given existing orderId, when findByOrderId is called, then return PaymentInfoDto")
        @Test
        void findByOrderIdSuccess() {
            when(paymentInfoJpaDao.findByOrderId(1L)).thenReturn(Optional.of(paymentInfoJpaEntity));

            Optional<PaymentInfoDto> result = paymentInfoRepository.findByOrderId(1L);

            assertTrue(result.isPresent());
            assertEquals("4111111111111111", result.get().cardNumber());
        }

        @DisplayName("Given non-existing orderId, when findByOrderId is called, then return empty")
        @Test
        void findByOrderIdNotFound() {
            when(paymentInfoJpaDao.findByOrderId(999L)).thenReturn(Optional.empty());

            Optional<PaymentInfoDto> result = paymentInfoRepository.findByOrderId(999L);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class DeleteByOrderIdTests {
        @DisplayName("Given existing orderId, when deleteByOrderId is called, then delete payment info")
        @Test
        void deleteByOrderIdSuccess() {
            doNothing().when(paymentInfoJpaDao).deleteByOrderId(1L);

            assertDoesNotThrow(() -> paymentInfoRepository.deleteByOrderId(1L));
            verify(paymentInfoJpaDao).deleteByOrderId(1L);
        }
    }
}
