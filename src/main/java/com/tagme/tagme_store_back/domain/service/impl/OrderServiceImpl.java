package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.EpsteinFiles.http.exception.ApiNotWorkingException;
import com.tagme.tagme_store_back.EpsteinFiles.payment.CreditCardPaymentService;
import com.tagme.tagme_store_back.EpsteinFiles.payment.records.CreditCardRequest;
import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.PaymentInfoDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.repository.OrderRepository;
import com.tagme.tagme_store_back.domain.repository.PaymentInfoRepository;
import com.tagme.tagme_store_back.domain.service.OrderService;
import com.tagme.tagme_store_back.domain.service.UserService;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final CreditCardPaymentService creditCardPaymentService;
    private final PaymentInfoRepository paymentInfoRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, 
                           CreditCardPaymentService creditCardPaymentService, 
                           PaymentInfoRepository paymentInfoRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.creditCardPaymentService = creditCardPaymentService;
        this.paymentInfoRepository = paymentInfoRepository;
    }

    @Override
    public List<OrderDto> getOrdersByUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new ValidationException("User ID must be a positive number");
        }

        // Verificar que el usuario existe
        userService.getById(userId);

        return orderRepository.getOrdersByUserId(userId);
    }

    @Override
    public Page<OrderDto> getAllOrders(int page, int size, OrderStatus status, Long userId) {
        if (page < 1) {
            throw new ValidationException("Page number must be at least 1");
        }
        if (size < 1) {
            throw new ValidationException("Page size must be at least 1");
        }
        
        return orderRepository.getAllOrders(page, size, status, userId);
    }

    @Override
    @Transactional(dontRollbackOn = BusinessException.class)
    public void retryPayment(Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new ValidationException("El ID del pedido debe ser un número positivo");
        }

        OrderDto order = orderRepository.getById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + orderId));

        if (order.orderStatus() != OrderStatus.PROCESSING) {
            throw new BusinessException("Solo se pueden reintentar pedidos en estado PROCESANDO");
        }

        // Obtener los datos de pago guardados
        Optional<PaymentInfoDto> paymentInfo = paymentInfoRepository.findByOrderId(orderId);
        
        if (paymentInfo.isEmpty()) {
            throw new BusinessException("No se encontraron datos de pago para este pedido. No se puede reintentar el pago.");
        }

        PaymentInfoDto payment = paymentInfo.get();
        CreditCardRequest creditCardRequest = new CreditCardRequest(
                payment.cardNumber(),
                payment.expirationDate(),
                payment.cvv(),
                payment.cardHolderName()
        );

        try {
            creditCardPaymentService.execute(creditCardRequest, order.totalPrice());
            
            // Si el pago es exitoso, marcar como PAYED
            OrderDto paidOrder = new OrderDto(
                    order.id(),
                    order.user(),
                    OrderStatus.PAYED,
                    order.orderItems(),
                    order.totalPrice(),
                    order.shippingCost(),
                    order.shippingInfo(),
                    LocalDateTime.now(),
                    order.createdAt()
            );
            orderRepository.save(paidOrder);
            
            // Eliminar los datos de pago después del éxito
            paymentInfoRepository.deleteByOrderId(orderId);
            
        } catch (ApiNotWorkingException e) {
            // Error 500 del banco - marcar como CANCELLED
            OrderDto cancelledOrder = new OrderDto(
                    order.id(),
                    order.user(),
                    OrderStatus.CANCELLED,
                    order.orderItems(),
                    order.totalPrice(),
                    order.shippingCost(),
                    order.shippingInfo(),
                    null,
                    order.createdAt()
            );
            orderRepository.save(cancelledOrder);
            
            // Eliminar los datos de pago
            paymentInfoRepository.deleteByOrderId(orderId);
            
            throw new BusinessException("El pago ha fallado. El pedido ha sido cancelado.");
        } catch (Exception e) {
            // Otros errores - marcar como CANCELLED
            OrderDto cancelledOrder = new OrderDto(
                    order.id(),
                    order.user(),
                    OrderStatus.CANCELLED,
                    order.orderItems(),
                    order.totalPrice(),
                    order.shippingCost(),
                    order.shippingInfo(),
                    null,
                    order.createdAt()
            );
            orderRepository.save(cancelledOrder);
            
            // Eliminar los datos de pago
            paymentInfoRepository.deleteByOrderId(orderId);
            
            throw new BusinessException("El pago ha fallado. El pedido ha sido cancelado.");
        }
    }

    @Override
    public Long getTotalOrders() {
        return orderRepository.countAll();
    }
}
