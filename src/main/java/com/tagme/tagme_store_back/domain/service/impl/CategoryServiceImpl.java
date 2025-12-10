package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.mapper.CategoryMapper;
import com.tagme.tagme_store_back.domain.model.Category;
import com.tagme.tagme_store_back.domain.repository.CategoryRepository;
import com.tagme.tagme_store_back.domain.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> getAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::fromCategoryDtoToCategory)
                .map(CategoryMapper::fromCategoryToCategoryDto).toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryMapper::fromCategoryDtoToCategory)
                .map(CategoryMapper::fromCategoryToCategoryDto)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        CategoryDto insertCategory = CategoryMapper
                .fromCategoryToCategoryDto(CategoryMapper
                        .fromCategoryDtoToCategory(categoryDto));

        return CategoryMapper
                .fromCategoryToCategoryDto(CategoryMapper
                        .fromCategoryDtoToCategory(categoryRepository.save(insertCategory)));
    }

    @Override
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
