package com.tagme.tagme_store_back.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.repository.ProductRepository;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.ProductJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.ProductJpaDao;
import com.tagme.tagme_store_back.persistence.mapper.ProductMapper;

public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaDao productDao;

    public ProductRepositoryImpl(ProductJpaDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public Page<ProductDto> findAll(int page, int size) {
        List<ProductDto> content = productDao.findAll(page, size).stream()
                .map(ProductMapper::fromProductJpaEntityToProductDto)
                .toList();

        long totalElements = productDao.count();
        return new Page<>(content, page, size, totalElements);
    }

    @Override
    public Page<ProductDto> findFilteredProducts(int page, int size, String name, Long categoryId, String material, Double minPrice, Double maxPrice) {
        List<ProductDto> content = productDao.findFilteredProducts(page, size, name, categoryId, material, minPrice, maxPrice).stream()
                .map(ProductMapper::fromProductJpaEntityToProductDto)
                .toList();

        long totalElements = productDao.countFilteredProducts(name, categoryId, material, minPrice, maxPrice);
        return new Page<>(content, page, size, totalElements);
    }

    @Override
    public Optional<ProductDto> findById(Long id) {
        return productDao.findById(id).map(ProductMapper::fromProductJpaEntityToProductDto);
    }

    @Override
    public List<ProductDto> findProductsByCategoryId(Long categoryId) {
        return productDao.findProductsByCategoryId(categoryId).stream().map(ProductMapper::fromProductJpaEntityToProductDto).toList();
    }

    @Override
    public void deleteById(Long id) {
        productDao.deleteById(id);
    }

    @Override
    public ProductDto save(ProductDto product) {
        ProductJpaEntity productJpaEntity = ProductMapper.fromProductDtoToProductJpaEntity(product);
        if (product.id() == null) {
            return ProductMapper.fromProductJpaEntityToProductDto(productDao.insert(productJpaEntity));
        }

        return ProductMapper.fromProductJpaEntityToProductDto(productDao.update(productJpaEntity));
    }

    @Override
    public Long getTotalProducts() {
        return productDao.count();
    }
}