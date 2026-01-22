package com.tagme.tagme_store_back.persistence.dao.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

import com.tagme.tagme_store_back.domain.model.ProductMaterial;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_products")
public class ProductJpaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;
    @Column(name = "discount_percentage", nullable = false)
    private BigDecimal discountPercentage;
    @Lob
    @Column(nullable = false)
    private byte[] image;
    @Column(name = "image_name", nullable = false)
    private String imageName;
    @OneToOne
    @JoinColumn(name = "category_id", nullable = true)
    private CategoryJpaEntity category;
    @Enumerated(EnumType.STRING)
    @Column(name = "material", nullable = true)
    private ProductMaterial material;
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ProductJpaEntity() {
    }

    public ProductJpaEntity(Long id, String name, String description, BigDecimal basePrice,
                            BigDecimal discountPercentage, byte[] image, String imageName, CategoryJpaEntity category, ProductMaterial material) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.discountPercentage = discountPercentage;
        this.image = image;
        this.imageName = imageName;
        this.category = category;
        this.material = material;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ProductMaterial getMaterial() {
        return material;
    }

    public void setMaterial(ProductMaterial material) {
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductJpaEntity that = (ProductJpaEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(basePrice, that.basePrice) && Objects.equals(discountPercentage, that.discountPercentage) && Objects.deepEquals(image, that.image) && Objects.equals(imageName, that.imageName) && Objects.equals(category, that.category) && material == that.material && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, basePrice, discountPercentage, Arrays.hashCode(image), imageName, category, material, createdAt);
    }

    public String getImageName() {
        return imageName;
    }


    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
