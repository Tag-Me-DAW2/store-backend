package com.tagme.tagme_store_back.domain.repository;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.model.Page;

import java.util.Optional;

public interface CategoryRepository {
    Page<CategoryDto> findAll(int page, int size);
    Optional<CategoryDto> findById(Long id);
    void deleteById(Long id);
    CategoryDto save(CategoryDto categoryDto);
}
