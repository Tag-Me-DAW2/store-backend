package com.tagme.tagme_store_back.persistence.dao.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class ProductJpaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private BigDecimal discountPercentage;
    @Lob
    private byte[] image;
    @OneToOne
    private CategoryJpaEntity category;

    public ProductJpaEntity() {
    }

    public ProductJpaEntity(Long id, String name, String description, BigDecimal basePrice,
            BigDecimal discountPercentage, byte[] image, CategoryJpaEntity category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.discountPercentage = discountPercentage;
        this.image = image;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public CategoryJpaEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryJpaEntity category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductJpaEntity that = (ProductJpaEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(basePrice, that.basePrice) && Objects.equals(discountPercentage, that.discountPercentage) && Objects.deepEquals(image, that.image) && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, basePrice, discountPercentage, Arrays.hashCode(image), category);
    }

}
