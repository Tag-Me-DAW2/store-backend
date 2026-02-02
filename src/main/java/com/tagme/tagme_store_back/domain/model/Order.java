package com.tagme.tagme_store_back.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private Long id;
    private User user;
    private OrderStatus orderStatus;
    private List<OrderItem> orderItems;
    private BigDecimal totalPrice;
    private LocalDateTime paidDate;
    private LocalDateTime createdAt;

    public Order(Long id, User user, OrderStatus orderStatus, List<OrderItem> orderItems, LocalDateTime paidDate, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.orderStatus = orderStatus;
        this.orderItems = orderItems;
        this.totalPrice = calculateTotalPrice();
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(user, order.user) && orderStatus == order.orderStatus && Objects.equals(orderItems, order.orderItems) && Objects.equals(totalPrice, order.totalPrice) && Objects.equals(paidDate, order.paidDate) && Objects.equals(createdAt, order.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, orderStatus, orderItems, totalPrice, paidDate, createdAt);
    }
}
