package com.tagme.tagme_store_back.domain.repository;

import com.tagme.tagme_store_back.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    void deleteById(Long id);
    Category save(Category category);
}
