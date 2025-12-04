package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.mapper.ProductMapper;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.model.Product;
import com.tagme.tagme_store_back.domain.repository.CategoryRepository;
import com.tagme.tagme_store_back.domain.repository.ProductRepository;
import com.tagme.tagme_store_back.domain.service.ProductService;
import jakarta.transaction.Transactional;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<ProductDto> getAll(int page, int size) {
        Page<ProductDto> productDtoPage = productRepository.findAll(page, size);

        List<ProductDto> itemsDto = productDtoPage.data()
                .stream()
                .map(ProductMapper::fromProductDtoToProduct)
                .map(ProductMapper::fromProductToProductDto)
                .toList();

        return new Page<>(
                itemsDto,
                productDtoPage.pageNumber(),
                productDtoPage.pageSize(),
                productDtoPage.totalElements()
        );
    }

    @Override
    public ProductDto getById(Long id) {
        if (id == null) {
            throw new RuntimeException("Id cannot be null");
        }

        return productRepository.findById(id)
                .map(ProductMapper::fromProductDtoToProduct)
                .map(ProductMapper::fromProductToProductDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    public List<ProductDto> getProductsByCategoryId(Long categoryId) {
        if (categoryId == null) {
            throw new RuntimeException("Category id cannot be null");
        }

        if (!categoryRepository.findById(categoryId).isPresent()) {
            throw new ResourceNotFoundException("Category with id: " + categoryId + " doesn't exist");
        }

        return productRepository.findProductsByCategoryId(categoryId).stream()
                .map(ProductMapper::fromProductDtoToProduct)
                .map(ProductMapper::fromProductToProductDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (id == null) {
            throw new RuntimeException("Id cannot be null");
        }

        productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " doesn't exist"));
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProductDto create(ProductDto product) {
        categoryRepository.findById(product.category().id()).orElseThrow(() -> new ResourceNotFoundException("Category with id: " + product.category().id() + " doesn't exist"));

        Product productModel = ProductMapper.fromProductDtoToProduct(product);
        ProductDto savedProductDto = productRepository.save(ProductMapper.fromProductToProductDto(productModel));
        return productRepository.save(savedProductDto);
    }

    @Override
    @Transactional
    public ProductDto update(ProductDto product) {
        productRepository.findById(product.id()).orElseThrow(() -> new ResourceNotFoundException("Product with id " + product.id() + " doesn't exist"));
        categoryRepository.findById(product.category().id()).orElseThrow(() -> new ResourceNotFoundException("Tried to update product with category " + product.category().id() + " but doesnt exist"));

        Product productModel = ProductMapper.fromProductDtoToProduct(product);
        ProductDto savedProductDto = productRepository.save(ProductMapper.fromProductToProductDto(productModel));
        return productRepository.save(savedProductDto);
    }

    @Override
    public Long getTotalProducts() {
        return productRepository.getTotalProducts();
    }
}
