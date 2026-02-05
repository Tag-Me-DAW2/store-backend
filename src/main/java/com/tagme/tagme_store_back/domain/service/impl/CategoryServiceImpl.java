package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.mapper.CategoryMapper;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.repository.CategoryRepository;
import com.tagme.tagme_store_back.domain.service.CategoryService;
import jakarta.transaction.Transactional;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<CategoryDto> getAll(int page, int size) {
        if(page < 1 || size < 1) {
            throw new BusinessException("Page and size must be greater than 0");
        }

        Page<CategoryDto> categoryPage = categoryRepository.findAll(page, size);

        List<CategoryDto> itemsDto = categoryPage.data().stream()
                .map(CategoryMapper::fromCategoryDtoToCategory)
                .map(CategoryMapper::fromCategoryToCategoryDto)
                .toList();

        return new Page<>(
                itemsDto,
                categoryPage.pageNumber(),
                categoryPage.pageSize(),
                categoryPage.totalElements()
        );
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryMapper::fromCategoryDtoToCategory)
                .map(CategoryMapper::fromCategoryToCategoryDto)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CategoryDto create(CategoryDto categoryDto) {
        CategoryDto insertCategory = CategoryMapper
                .fromCategoryToCategoryDto(CategoryMapper
                        .fromCategoryDtoToCategory(categoryDto));

        return CategoryMapper
                .fromCategoryToCategoryDto(CategoryMapper
                        .fromCategoryDtoToCategory(categoryRepository.save(insertCategory)));
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto categoryDto) {
        categoryRepository.findById(categoryDto.id()).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryDto.id()));
        CategoryDto updateCategory = CategoryMapper
                .fromCategoryToCategoryDto(CategoryMapper
                        .fromCategoryDtoToCategory(categoryDto));

        return CategoryMapper
                .fromCategoryToCategoryDto(CategoryMapper
                        .fromCategoryDtoToCategory(categoryRepository.save(updateCategory)));
    }
}
