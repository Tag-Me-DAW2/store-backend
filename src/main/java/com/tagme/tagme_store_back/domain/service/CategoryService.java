package com.tagme.tagme_store_back.domain.service;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAll();
    CategoryDto getById(Long id);
    void deleteById(Long id);
    CategoryDto create(CategoryDto categoryDto);
    CategoryDto update(CategoryDto categoryDto);
}
