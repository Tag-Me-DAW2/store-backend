package com.tagme.tagme_store_back.domain.mapper;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.model.Category;

public class CategoryMapper {
    public static CategoryDto fromCategoryToCategoryDto(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public static Category fromCategoryDtoToCategory (CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }

        return new Category(
                categoryDto.id(),
                categoryDto.name()
        );
    }
}
