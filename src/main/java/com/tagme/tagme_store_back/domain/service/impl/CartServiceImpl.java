package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.EpsteinFiles.http.exception.ApiNotWorkingException;
import com.tagme.tagme_store_back.EpsteinFiles.payment.CreditCardPaymentService;
import com.tagme.tagme_store_back.EpsteinFiles.payment.records.CreditCardRequest;
import com.tagme.tagme_store_back.domain.dto.*;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.mapper.OrderMapper;
import com.tagme.tagme_store_back.domain.mapper.UserMapper;
import com.tagme.tagme_store_back.domain.model.Order;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.repository.OrderRepository;
import com.tagme.tagme_store_back.domain.repository.PaymentInfoRepository;
import com.tagme.tagme_store_back.domain.service.CartService;
import com.tagme.tagme_store_back.domain.service.ProductService;
import com.tagme.tagme_store_back.domain.service.UserService;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CartServiceImpl implements CartService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final CreditCardPaymentService creditCardPaymentService;
    private final PaymentInfoRepository paymentInfoRepository;

    public CartServiceImpl(OrderRepository orderRepository, UserService userService, ProductService productService, CreditCardPaymentService creditCardPaymentService, PaymentInfoRepository paymentInfoRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productService = productService;
        this.creditCardPaymentService = creditCardPaymentService;
        this.paymentInfoRepository = paymentInfoRepository;
    }

    @Override
    @Transactional
    public void createCart(Long userId) {
        if (userId == null || userId <= 0) {
            throw new ValidationException("User ID must be a positive number");
        }

        UserDto user = userService.getById(userId);

        Order newCart = new Order(
                null,
                UserMapper.fromUserDtoToUser(user),
                OrderStatus.PENDING,
                new ArrayList<>(),
                null,
                null,
                null,
                LocalDateTime.now()
        );

        OrderDto newCartDto = OrderMapper.fromOrderToOrderDto(newCart);
        orderRepository.save(newCartDto);
    }

    @Override
    @Transactional
    public OrderDto getActiveCart(Long userId) {
        if (userId == null || userId <= 0) {
            throw new ValidationException("User ID must be a positive number");
        }

        userService.getById(userId);

        return orderRepository.getActiveOrder(userId)
                .map(OrderMapper::fromOrderDtoToOrder)
                .map(OrderMapper::fromOrderToOrderDto)
                .orElseGet(() -> {
                    createCart(userId);
                    return orderRepository.getActiveOrder(userId)
                            .map(OrderMapper::fromOrderDtoToOrder)
                            .map(OrderMapper::fromOrderToOrderDto)
                            .orElseThrow(() -> new BusinessException("Failed to create cart for user with id: " + userId));
                });
    }

    @Override
    public OrderStatus getCartStatus(Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new ValidationException("Order ID must be a positive number");
        }

        return orderRepository.getStatus(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + orderId));
    }

    @Override
    @Transactional
    public void updatePendingCart(OrderDto orderDto) {
        if (orderDto == null) {
            throw new ValidationException("Order cannot be null");
        }
        if (orderDto.user() == null || orderDto.user().id() == null || orderDto.user().id() <= 0) {
            throw new ValidationException("User ID must be a positive number");
        }

        UserDto user = userService.getById(orderDto.user().id());
        OrderDto activeCart = getActiveCart(orderDto.user().id());

        if (activeCart.orderStatus() != OrderStatus.PENDING) {
            throw new BusinessException("Cannot update cart. Cart is not in PENDING status");
        }

        if (orderDto.orderItems() != null) {
            Set<Long> productIds = new HashSet<>();
            for (OrderItemDto item : orderDto.orderItems()) {
                if (item.productDto() == null || item.productDto().id() == null || item.productDto().id() <= 0) {
                    throw new ValidationException("Product ID must be a positive number");
                }
                if (item.quantity() == null || item.quantity() < 1) {
                    throw new ValidationException("Quantity must be at least 1");
                }
                if (!productIds.add(item.productDto().id())) {
                    throw new ValidationException("Duplicate product in cart: " + item.productDto().id());
                }
            }
        }

        List<OrderItemDto> resolvedItems = new ArrayList<>();
        if (orderDto.orderItems() != null && !orderDto.orderItems().isEmpty()) {
            for (OrderItemDto item : orderDto.orderItems()) {
                ProductDto fullProduct = productService.getById(item.productDto().id());
                // Para PENDING: los datos del producto se obtienen del Product actual
                OrderItemDto resolvedItem = new OrderItemDto(
                        item.id(),
                        fullProduct,
                        fullProduct.name(),
                        fullProduct.image(),
                        fullProduct.imageName(),
                        item.quantity(),
                        fullProduct.basePrice(),
                        fullProduct.discountPercentage(),
                        item.total()
                );
                resolvedItems.add(resolvedItem);
            }
        }

        OrderDto orderWithResolvedItems = new OrderDto(
                orderDto.id(),
                orderDto.user(),
                orderDto.orderStatus(),
                resolvedItems,
                orderDto.totalPrice(),
                null,
                orderDto.shippingInfo(),
                orderDto.paidDate(),
                orderDto.createdAt()
        );

        Order orderModel = OrderMapper.fromOrderDtoToOrder(orderWithResolvedItems);
        OrderDto orderToUpdate = OrderMapper.fromOrderToOrderDto(orderModel);

        OrderDto updatedCart = new OrderDto(
                activeCart.id(),
                user,
                OrderStatus.PENDING,
                orderToUpdate.orderItems(),
                orderToUpdate.totalPrice(),
                orderToUpdate.shippingCost(),
                orderDto.shippingInfo(),
                null,
                activeCart.createdAt()
        );

        orderRepository.save(updatedCart);
    }

    @Override
    @Transactional
    public void updateCart(OrderDto orderDto) {
        if (orderDto == null) {
            throw new ValidationException("Order cannot be null");
        }
        if (orderDto.user() == null || orderDto.user().id() == null || orderDto.user().id() <= 0) {
            throw new ValidationException("User ID must be a positive number");
        }

        UserDto user = userService.getById(orderDto.user().id());
        OrderDto activeCart = getActiveCart(orderDto.user().id());

        OrderStatus currentStatus = activeCart.orderStatus();
        OrderStatus newStatus = orderDto.orderStatus();

        if (currentStatus == OrderStatus.PAYED) {
            throw new BusinessException("Cannot update a cart that is already " + currentStatus);
        }

        if (currentStatus == OrderStatus.PENDING && newStatus != OrderStatus.PENDING && newStatus != OrderStatus.PROCESSING) {
            throw new BusinessException("Invalid status transition from PENDING to " + newStatus);
        }

        if (currentStatus == OrderStatus.PROCESSING && newStatus != OrderStatus.PROCESSING && newStatus != OrderStatus.PAYED) {
            throw new BusinessException("Invalid status transition from PROCESSING to " + newStatus);
        }

        if (orderDto.orderItems() != null) {
            Set<Long> productIds = new HashSet<>();
            for (OrderItemDto item : orderDto.orderItems()) {
                if (item.productDto() == null || item.productDto().id() == null || item.productDto().id() <= 0) {
                    throw new ValidationException("Product ID must be a positive number");
                }
                if (item.quantity() == null || item.quantity() < 1) {
                    throw new ValidationException("Quantity must be at least 1");
                }
                if (!productIds.add(item.productDto().id())) {
                    throw new ValidationException("Duplicate product in cart: " + item.productDto().id());
                }
            }
        }

        List<OrderItemDto> resolvedItems = new ArrayList<>();
        if (orderDto.orderItems() != null && !orderDto.orderItems().isEmpty()) {
            for (OrderItemDto item : orderDto.orderItems()) {
                ProductDto fullProduct = productService.getById(item.productDto().id());
                // Los datos del producto se obtienen del Product actual
                OrderItemDto resolvedItem = new OrderItemDto(
                        item.id(),
                        fullProduct,
                        fullProduct.name(),
                        fullProduct.image(),
                        fullProduct.imageName(),
                        item.quantity(),
                        fullProduct.basePrice(),
                        fullProduct.discountPercentage(),
                        item.total()
                );
                resolvedItems.add(resolvedItem);
            }
        }

        OrderDto orderWithResolvedItems = new OrderDto(
                orderDto.id(),
                orderDto.user(),
                orderDto.orderStatus(),
                resolvedItems,
                orderDto.totalPrice(),
                null,
                orderDto.shippingInfo(),
                orderDto.paidDate(),
                orderDto.createdAt()
        );

        Order orderModel = OrderMapper.fromOrderDtoToOrder(orderWithResolvedItems);
        OrderDto orderToUpdate = OrderMapper.fromOrderToOrderDto(orderModel);

        LocalDateTime paidDate = (orderToUpdate.orderStatus() == OrderStatus.PAYED) ? LocalDateTime.now() : null;

        OrderDto updatedCart = new OrderDto(
                activeCart.id(),
                user,
                orderToUpdate.orderStatus(),
                orderToUpdate.orderItems(),
                orderToUpdate.totalPrice(),
                orderToUpdate.shippingCost(),
                orderDto.shippingInfo(),
                paidDate,
                activeCart.createdAt()
        );

        orderRepository.save(updatedCart);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        if (userId == null || userId <= 0) {
            throw new ValidationException("User ID must be a positive number");
        }

        userService.getById(userId);
        OrderDto activeCart = getActiveCart(userId);

        if (activeCart.orderStatus() != OrderStatus.PENDING) {
            throw new BusinessException("Cannot clear cart. Cart is not in PENDING status");
        }

        OrderDto clearedCart = new OrderDto(
                activeCart.id(),
                activeCart.user(),
                OrderStatus.PENDING,
                new ArrayList<>(),
                BigDecimal.ZERO,
                null,
                null,
                null,
                activeCart.createdAt()
        );

        orderRepository.save(clearedCart);
    }

    @Override
    @Transactional
    public void payWithCreditCard(Long userId, PayCartDto payCartDto) {
        if (userId == null) {
            throw new ValidationException("User ID cannot be null");
        }

        if (payCartDto == null) {
            throw new ValidationException("Payment information cannot be null");
        }

        OrderDto activeCart = getActiveCart(userId);

        CreditCardRequest creditCardRequest = new CreditCardRequest(
                payCartDto.paymentInfo().cardNumber(),
                payCartDto.paymentInfo().expirationDate(),
                payCartDto.paymentInfo().cvv(),
                payCartDto.paymentInfo().cardHolderName()
        );

        // Actualizar el estado del carrito a PROCESSING antes de realizar el pago para evitar que se realicen cambios en el carrito durante el proceso de pago
        OrderDto processingCart = new OrderDto(
                activeCart.id(),
                activeCart.user(),
                OrderStatus.PROCESSING,
                activeCart.orderItems(),
                activeCart.totalPrice(),
                payCartDto.orderInfo().shippingCost(),
                payCartDto.shippingInfo(),
                null,
                activeCart.createdAt()
        );
        orderRepository.save(processingCart);

        // Guardar datos de pago para posible reintento
        PaymentInfoDto paymentInfo = new PaymentInfoDto(
                null,
                processingCart.id(),
                payCartDto.paymentInfo().cardNumber(),
                payCartDto.paymentInfo().cardHolderName(),
                payCartDto.paymentInfo().cvv(),
                payCartDto.paymentInfo().expirationDate()
        );
        paymentInfoRepository.save(paymentInfo);

        // Realizar el pago.
        try {
            creditCardPaymentService.execute(creditCardRequest, activeCart.totalPrice());
        } catch (Exception e) {
            if (e instanceof ApiNotWorkingException) {
                // Mantener en PROCESSING para poder reintentar
                return;
            } else throw new RuntimeException(e.getMessage());
        }

        // Si el pago ha ido bien, actualizar el estado del carrito a PAYED y eliminar datos de pago
        OrderDto paidCart = new OrderDto(
                processingCart.id(),
                processingCart.user(),
                OrderStatus.PAYED,
                processingCart.orderItems(),
                processingCart.totalPrice(),
                processingCart.shippingCost(),
                processingCart.shippingInfo(),
                LocalDateTime.now(),
                processingCart.createdAt()
        );
        orderRepository.save(paidCart);
        
        // Eliminar datos de pago ya que el pago fue exitoso
        paymentInfoRepository.deleteByOrderId(processingCart.id());
    }
}
