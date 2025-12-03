package com.tagme.tagme_store_back.domain.repository;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.model.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> findAll();
    Category findById(Long id);
    void deleteById(Long id);
    Category save(Category category);
    Category update(Long id, Category category);
}
