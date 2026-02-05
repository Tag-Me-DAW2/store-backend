package com.tagme.tagme_store_back.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private static final BigDecimal BASE_SHIPPING_COST = new BigDecimal("5.00");
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("50.00");

    private Long id;
    private User user;
    private OrderStatus orderStatus;
    private List<OrderItem> orderItems;
    private BigDecimal totalPrice;
    private BigDecimal shippingCost;
    private ShippingInfo shippingInfo;
    private LocalDateTime paidDate;
    private LocalDateTime createdAt;

    public Order(Long id, User user, OrderStatus orderStatus, List<OrderItem> orderItems, BigDecimal shippingCost, ShippingInfo shippingInfo, LocalDateTime paidDate, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.orderStatus = orderStatus;
        this.orderItems = orderItems;
        this.totalPrice = calculateTotalPrice();
        this.shippingCost = shippingCost != null ? shippingCost : calculateShippingCost();
        this.shippingInfo = shippingInfo;
        this.paidDate = paidDate;
        this.createdAt = createdAt;
    }

    private BigDecimal calculateTotalPrice() {
        if (orderItems == null || orderItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return orderItems.stream()
                .map(OrderItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula el coste de envío: 5€ base, gratis si el total supera 50€
     */
    public BigDecimal calculateShippingCost() {
        BigDecimal itemsTotal = calculateTotalPrice();
        if (itemsTotal.compareTo(FREE_SHIPPING_THRESHOLD) >= 0) {
            return BigDecimal.ZERO;
        }
        return BASE_SHIPPING_COST;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDateTime paidDate) {
        this.paidDate = paidDate;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }

    public ShippingInfo getShippingInfo() {
        return shippingInfo;
    }

    public void setShippingInfo(ShippingInfo shippingInfo) {
        this.shippingInfo = shippingInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(user, order.user) && orderStatus == order.orderStatus && Objects.equals(orderItems, order.orderItems) && Objects.equals(totalPrice, order.totalPrice) && Objects.equals(shippingCost, order.shippingCost) && Objects.equals(shippingInfo, order.shippingInfo) && Objects.equals(paidDate, order.paidDate) && Objects.equals(createdAt, order.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, orderStatus, orderItems, totalPrice, shippingCost, shippingInfo, paidDate, createdAt);
    }
}
