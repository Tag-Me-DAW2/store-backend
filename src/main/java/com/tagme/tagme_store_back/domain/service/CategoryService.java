package com.tagme.tagme_store_back.domain.service;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.model.Page;

public interface CategoryService {
    Page<CategoryDto> getAll(int page, int size);
    CategoryDto getById(Long id);
    void deleteById(Long id);
    CategoryDto create(CategoryDto categoryDto);
    CategoryDto update(CategoryDto categoryDto);
}
