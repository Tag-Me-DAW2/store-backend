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

    // Campos de snapshot del producto (para historial)
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Lob
    @Column(name = "product_image")
    private byte[] productImage;

    @Column(name = "product_image_name")
    private String productImageName;

    @Column(name = "base_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "discountPercentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(nullable = false)
    private Long quantity;

    public OrderItemJpaEntity() {}

    public OrderItemJpaEntity(Long id, OrderJpaEntity order, ProductJpaEntity product,
                              String productName, byte[] productImage, String productImageName,
                              BigDecimal basePrice, BigDecimal discountPercentage, Long quantity) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.productName = productName;
        this.productImage = productImage;
        this.productImageName = productImageName;
        this.basePrice = basePrice;
        this.discountPercentage = discountPercentage;
        this.quantity = quantity;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public byte[] getProductImage() {
        return productImage;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    public String getProductImageName() {
        return productImageName;
    }

    public void setProductImageName(String productImageName) {
        this.productImageName = productImageName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    /**
     * Obtiene el precio base. Si la orden está PENDING, usa el precio actual del producto.
     * Si la orden ya está procesada, usa el precio guardado (snapshot).
     */
    public BigDecimal getBasePrice() {
        if (order != null && order.getOrderStatus() == OrderStatus.PENDING && product != null) {
            return product.getBasePrice();
        }
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    /**
     * Obtiene el porcentaje de descuento. Si la orden está PENDING, usa el descuento actual del producto.
     * Si la orden ya está procesada, usa el descuento guardado (snapshot).
     */
    public BigDecimal getDiscountPercentage() {
        if (order != null && order.getOrderStatus() == OrderStatus.PENDING && product != null) {
            return product.getDiscountPercentage();
        }
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    // Métodos para obtener los valores persistidos directamente
    public BigDecimal getPersistedBasePrice() {
        return basePrice;
    }

    public BigDecimal getPersistedDiscountPercentage() {
        return discountPercentage;
    }

    /**
     * Copia los datos actuales del producto al snapshot para persistir el historial.
     */
    public void snapshotProductData() {
        if (product != null) {
            this.productName = product.getName();
            this.productImage = product.getImage();
            this.productImageName = product.getImageName();
            this.basePrice = product.getBasePrice();
            this.discountPercentage = product.getDiscountPercentage();
        }
    }
}
