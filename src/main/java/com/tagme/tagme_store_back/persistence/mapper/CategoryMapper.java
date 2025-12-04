package com.tagme.tagme_store_back.persistence.mapper;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.CategoryJpaEntity;

public class CategoryMapper {
    public static CategoryJpaEntity fromCategoryDtoToCategoryJpaEntity(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }
        return new CategoryJpaEntity(
                categoryDto.id(),
                categoryDto.name()
        );
    }

    public static CategoryDto fromCategoryJpaEntityToCategoryDto(CategoryJpaEntity categoryJpaEntity) {
        if (categoryJpaEntity == null) {
            return null;
        }

        return new CategoryDto(
                categoryJpaEntity.getId(),
                categoryJpaEntity.getName()
        );
    }
}
