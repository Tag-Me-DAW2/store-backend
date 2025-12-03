package com.tagme.tagme_store_back.domain.mapper;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.model.Category;

public class CategoryMapper {
    private static CategoryMapper INSTANCE;

    private CategoryMapper() {
    }

    public static CategoryMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CategoryMapper();
        }
        return INSTANCE;
    }

    public CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public Category toModel(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }

        return new Category(
                categoryDto.id(),
                categoryDto.name()
        );
    }
}
