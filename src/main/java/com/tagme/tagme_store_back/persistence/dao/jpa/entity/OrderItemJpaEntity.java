package com.tagme.tagme_store_back.persistence.dao.jpa.entity;

import com.tagme.tagme_store_back.domain.model.OrderStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_order_items",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"order_id", "product_id"})
        })
public class OrderItemJpaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderJpaEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductJpaEntity product;

    @Column(nullable = false)
    private Long quantity;

    private BigDecimal basePrice;

    @Column(name = "discount_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercentage;


    public OrderItemJpaEntity() {}

    public OrderItemJpaEntity(Long id, OrderJpaEntity order, ProductJpaEntity product, Long quantity, BigDecimal basePrice, BigDecimal discountPercentage) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.basePrice = basePrice;
        this.discountPercentage = discountPercentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderJpaEntity getOrder() {
        return order;
    }

    public void setOrder(OrderJpaEntity order) {
        this.order = order;
    }

    public ProductJpaEntity getProduct() {
        return product;
    }

    public void setProduct(ProductJpaEntity product) {
        this.product = product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Transient
    public BigDecimal getBasePrice() {
        if (order.getOrderStatus() == OrderStatus.PENDING) {
            return product.getBasePrice();
        }
        return basePrice;
    }

    @Transient
    public BigDecimal getDiscountPercentage() {
        if (order.getOrderStatus() == OrderStatus.PENDING) {
            return product.getDiscountPercentage();
        }
        return discountPercentage;
    }

    public BigDecimal getPersistedBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getPersistedDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}
