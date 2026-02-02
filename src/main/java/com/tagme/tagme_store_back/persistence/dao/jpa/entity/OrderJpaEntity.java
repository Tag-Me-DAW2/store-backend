package com.tagme.tagme_store_back.persistence.dao.jpa.entity;

import com.tagme.tagme_store_back.domain.model.OrderStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_orders")
public class OrderJpaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usr_id")
    private UserJpaEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItemJpaEntity> orderItems = new ArrayList<>();

    @Column(name = "paid-date")
    private LocalDateTime paidDate;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public OrderJpaEntity() {
    }

    public OrderJpaEntity(Long id, UserJpaEntity user, OrderStatus orderStatus, List<OrderItemJpaEntity> orderItems) {
        this.id = id;
        this.user = user;
        this.orderStatus = orderStatus;
        if (orderItems != null) {
            addItems(orderItems);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserJpaEntity getUser() {
        return user;
    }

    public void setUser(UserJpaEntity user) {
        this.user = user;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderItemJpaEntity> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemJpaEntity> orderItems) {
        this.orderItems = orderItems;
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

    public void addItem(OrderItemJpaEntity item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItemJpaEntity item) {
        orderItems.remove(item);
        item.setOrder(null);
    }

    public void addItems(List<OrderItemJpaEntity> items) {
        for (OrderItemJpaEntity item : items) {
            addItem(item);
        }
    }

}
