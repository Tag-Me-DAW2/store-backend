package com.tagme.tagme_store_back.domain.service;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAll();
    CategoryDto getById(Long id);
    List<CategoryDto> getSubcategoriesByCategoryId(Long categoryId);
    void deleteById(Long id);
    CategoryDto create(CategoryDto categoryDto);
    CategoryDto update(Long id, CategoryDto categoryDto);
}
