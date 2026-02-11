package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.persistence.dao.jpa.OrderJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderJpaEntity;
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
class OrderRepositoryImplTest {

    @Mock
    private OrderJpaDao orderJpaDao;

    @InjectMocks
    private OrderRepositoryImpl orderRepository;

    private OrderJpaEntity orderJpaEntity;
    private UserJpaEntity userJpaEntity;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userJpaEntity = Instancio.of(UserJpaEntity.class)
                .set(field(UserJpaEntity::getId), 1L)
                .create();
        
        orderJpaEntity = new OrderJpaEntity();
        orderJpaEntity.setId(1L);
        orderJpaEntity.setUser(userJpaEntity);
        orderJpaEntity.setOrderStatus(OrderStatus.PENDING);
        orderJpaEntity.setTotalPrice(BigDecimal.valueOf(100));
        orderJpaEntity.setOrderItems(new ArrayList<>());
        orderJpaEntity.setCreatedAt(LocalDateTime.now());

        userDto = Instancio.of(UserDto.class)
                .set(field(UserDto::id), 1L)
                .create();
    }

    @Nested
    class SaveTests {
        @DisplayName("Given new order, when save is called, then insert and return OrderDto")
        @Test
        void saveNewOrder() {
            OrderDto orderDto = new OrderDto(
                    null,
                    userDto,
                    OrderStatus.PENDING,
                    new ArrayList<>(),
                    BigDecimal.valueOf(100),
                    null,
                    null,
                    null,
                    LocalDateTime.now()
            );

            when(orderJpaDao.insert(any())).thenReturn(orderJpaEntity);

            OrderDto result = orderRepository.save(orderDto);

            assertNotNull(result);
            verify(orderJpaDao).insert(any());
        }

        @DisplayName("Given existing order, when save is called, then update and return OrderDto")
        @Test
        void saveExistingOrder() {
            OrderDto orderDto = new OrderDto(
                    1L,
                    userDto,
                    OrderStatus.PENDING,
                    new ArrayList<>(),
                    BigDecimal.valueOf(100),
                    null,
                    null,
                    null,
                    LocalDateTime.now()
            );

            when(orderJpaDao.update(any())).thenReturn(orderJpaEntity);

            OrderDto result = orderRepository.save(orderDto);

            assertNotNull(result);
            verify(orderJpaDao).update(any());
        }

        @DisplayName("Given null orderDto, when save is called, then throw ValidationException")
        @Test
        void saveNullOrder() {
            assertThrows(ValidationException.class, () -> orderRepository.save(null));
        }

        @DisplayName("Given orderDto with null user, when save is called, then throw ValidationException")
        @Test
        void saveOrderWithNullUser() {
            OrderDto orderDto = new OrderDto(null, null, OrderStatus.PENDING, new ArrayList<>(), null, null, null, null, null);
            assertThrows(ValidationException.class, () -> orderRepository.save(orderDto));
        }

        @DisplayName("Given orderDto with null status, when save is called, then throw ValidationException")
        @Test
        void saveOrderWithNullStatus() {
            OrderDto orderDto = new OrderDto(null, userDto, null, new ArrayList<>(), null, null, null, null, null);
            assertThrows(ValidationException.class, () -> orderRepository.save(orderDto));
        }
    }

    @Nested
    class DeleteTests {
        @DisplayName("Given valid orderId, when delete is called, then delete order")
        @Test
        void deleteSuccess() {
            doNothing().when(orderJpaDao).deleteById(1L);

            assertDoesNotThrow(() -> orderRepository.delete(1L));
            verify(orderJpaDao).deleteById(1L);
        }

        @DisplayName("Given null orderId, when delete is called, then throw ValidationException")
        @Test
        void deleteNullId() {
            assertThrows(ValidationException.class, () -> orderRepository.delete(null));
        }

        @DisplayName("Given invalid orderId, when delete is called, then throw ValidationException")
        @Test
        void deleteInvalidId() {
            assertThrows(ValidationException.class, () -> orderRepository.delete(0L));
            assertThrows(ValidationException.class, () -> orderRepository.delete(-1L));
        }
    }

    @Nested
    class GetStatusTests {
        @DisplayName("Given valid orderId, when getStatus is called, then return status")
        @Test
        void getStatusSuccess() {
            when(orderJpaDao.getOrderStatus(1L)).thenReturn(Optional.of(OrderStatus.PENDING));

            Optional<OrderStatus> result = orderRepository.getStatus(1L);

            assertTrue(result.isPresent());
            assertEquals(OrderStatus.PENDING, result.get());
        }

        @DisplayName("Given null orderId, when getStatus is called, then throw ValidationException")
        @Test
        void getStatusNullId() {
            assertThrows(ValidationException.class, () -> orderRepository.getStatus(null));
        }

        @DisplayName("Given invalid orderId, when getStatus is called, then throw ValidationException")
        @Test
        void getStatusInvalidId() {
            assertThrows(ValidationException.class, () -> orderRepository.getStatus(0L));
        }
    }

    @Nested
    class GetActiveOrderTests {
        @DisplayName("Given valid userId with active order, when getActiveOrder is called, then return order")
        @Test
        void getActiveOrderSuccess() {
            when(orderJpaDao.findActiveOrderByUserId(1L)).thenReturn(Optional.of(orderJpaEntity));

            Optional<OrderDto> result = orderRepository.getActiveOrder(1L);

            assertTrue(result.isPresent());
        }

        @DisplayName("Given null userId, when getActiveOrder is called, then throw ValidationException")
        @Test
        void getActiveOrderNullUserId() {
            assertThrows(ValidationException.class, () -> orderRepository.getActiveOrder(null));
        }

        @DisplayName("Given invalid userId, when getActiveOrder is called, then throw ValidationException")
        @Test
        void getActiveOrderInvalidUserId() {
            assertThrows(ValidationException.class, () -> orderRepository.getActiveOrder(0L));
        }
    }

    @Nested
    class GetOrdersByUserIdTests {
        @DisplayName("Given valid userId, when getOrdersByUserId is called, then return orders")
        @Test
        void getOrdersByUserIdSuccess() {
            when(orderJpaDao.findNonActiveOrdersByUserId(1L)).thenReturn(List.of(orderJpaEntity));

            List<OrderDto> result = orderRepository.getOrdersByUserId(1L);

            assertNotNull(result);
            assertEquals(1, result.size());
        }

        @DisplayName("Given null userId, when getOrdersByUserId is called, then throw ValidationException")
        @Test
        void getOrdersByUserIdNullId() {
            assertThrows(ValidationException.class, () -> orderRepository.getOrdersByUserId(null));
        }
    }

    @Nested
    class GetAllOrdersTests {
        @DisplayName("Given valid parameters, when getAllOrders is called, then return page")
        @Test
        void getAllOrdersSuccess() {
            when(orderJpaDao.findAllWithFilters(1, 10, null, null)).thenReturn(List.of(orderJpaEntity));
            when(orderJpaDao.countWithFilters(null, null)).thenReturn(1L);

            Page<OrderDto> result = orderRepository.getAllOrders(1, 10, null, null);

            assertNotNull(result);
            assertEquals(1, result.data().size());
            assertEquals(1L, result.totalElements());
        }

        @DisplayName("Given status filter, when getAllOrders is called, then filter is applied")
        @Test
        void getAllOrdersWithStatusFilter() {
            when(orderJpaDao.findAllWithFilters(1, 10, OrderStatus.PROCESSING, null)).thenReturn(List.of(orderJpaEntity));
            when(orderJpaDao.countWithFilters(OrderStatus.PROCESSING, null)).thenReturn(1L);

            Page<OrderDto> result = orderRepository.getAllOrders(1, 10, OrderStatus.PROCESSING, null);

            assertNotNull(result);
            verify(orderJpaDao).findAllWithFilters(1, 10, OrderStatus.PROCESSING, null);
        }

        @DisplayName("Given userId filter, when getAllOrders is called, then filter is applied")
        @Test
        void getAllOrdersWithUserIdFilter() {
            when(orderJpaDao.findAllWithFilters(1, 10, null, 1L)).thenReturn(List.of(orderJpaEntity));
            when(orderJpaDao.countWithFilters(null, 1L)).thenReturn(1L);

            Page<OrderDto> result = orderRepository.getAllOrders(1, 10, null, 1L);

            assertNotNull(result);
            verify(orderJpaDao).findAllWithFilters(1, 10, null, 1L);
        }
    }

    @Nested
    class GetByIdTests {
        @DisplayName("Given valid orderId, when getById is called, then return order")
        @Test
        void getByIdSuccess() {
            when(orderJpaDao.findById(1L)).thenReturn(Optional.of(orderJpaEntity));

            Optional<OrderDto> result = orderRepository.getById(1L);

            assertTrue(result.isPresent());
        }

        @DisplayName("Given non-existing orderId, when getById is called, then return empty")
        @Test
        void getByIdNotFound() {
            when(orderJpaDao.findById(999L)).thenReturn(Optional.empty());

            Optional<OrderDto> result = orderRepository.getById(999L);

            assertTrue(result.isEmpty());
        }

        @DisplayName("Given null orderId, when getById is called, then throw ValidationException")
        @Test
        void getByIdNullId() {
            assertThrows(ValidationException.class, () -> orderRepository.getById(null));
        }
    }

    @Nested
    class CountAllTests {
        @DisplayName("When countAll is called, then return total count")
        @Test
        void countAllSuccess() {
            when(orderJpaDao.countWithFilters(null, null)).thenReturn(10L);

            Long result = orderRepository.countAll();

            assertEquals(10L, result);
        }
    }
}
