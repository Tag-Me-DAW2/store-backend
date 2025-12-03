package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.mapper.ProductMapper;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.model.Product;
import com.tagme.tagme_store_back.domain.repository.ProductRepository;
import com.tagme.tagme_store_back.domain.service.ProductService;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<ProductDto> getAll(int page, int size) {
        List<Product> products = productRepository.findAll(page, size);
        long totalProducts = productRepository.getTotalProducts();

        return new Page<>(
                products.stream()
                        .map(ProductMapper.getInstance()::toDto)
                        .toList(),
                page,
                size,
                totalProducts
        );
    }

    @Override
    public long getTotalProducts() {
        return productRepository.getTotalProducts();
    }

    @Override
    public ProductDto getById(Long id) {
        return ProductMapper.getInstance().toDto(productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id)));
    }

    @Override
    public List<ProductDto> getProductsByCategoryId(Long categoryId) {
        return productRepository.findProductsByCategoryId(categoryId).stream().map(ProductMapper.getInstance()::toDto).toList();
    }

    @Override
    public void deleteById(Long id) {
        productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " doesn't exist"));
        productRepository.deleteById(id);
    }

    @Override
    public ProductDto create(ProductDto product) {
        return ProductMapper.getInstance().toDto(productRepository.save(ProductMapper.getInstance().toModel(product)));
    }

    @Override
    public ProductDto update(ProductDto product) {
        return ProductMapper.getInstance().toDto(productRepository.update(ProductMapper.getInstance().toModel(product)));
    }
}
