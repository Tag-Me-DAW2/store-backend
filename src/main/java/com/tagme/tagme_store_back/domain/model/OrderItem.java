package com.tagme.tagme_store_back.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class OrderItem {
    private Long id;
    private Product product;
    private Order order;
    private Long quantity;
    private BigDecimal basePrice;
    private BigDecimal discountPercentage;
    private BigDecimal total;

    public OrderItem(Long id, Product product, Order order, Long quantity, BigDecimal basePrice, BigDecimal discountPercentage) {
        this.id = id;
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.basePrice = basePrice;
        this.discountPercentage = discountPercentage;
        this.total = calculateFinalPrice();
    }

    private BigDecimal calculateFinalPrice() {

        BigDecimal hundred = BigDecimal.valueOf(100);

        BigDecimal discount = basePrice
                .multiply(discountPercentage)
                .divide(hundred, 4, RoundingMode.HALF_UP);

        BigDecimal finalUnitPrice = basePrice.subtract(discount);

        return finalUnitPrice
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id) && Objects.equals(product, orderItem.product) && Objects.equals(order, orderItem.order) && Objects.equals(quantity, orderItem.quantity) && Objects.equals(basePrice, orderItem.basePrice) && Objects.equals(discountPercentage, orderItem.discountPercentage) && Objects.equals(total, orderItem.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, order, quantity, basePrice, discountPercentage, total);
    }
}
