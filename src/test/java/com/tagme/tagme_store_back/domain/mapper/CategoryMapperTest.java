package com.tagme.tagme_store_back.domain.mapper;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.model.Category;
import org.instancio.Instancio;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    @Nested
    class fromCategoryToCategoryDtoTests {

        @Test
        void fromCategoryToCategoryDto_NullInput_ReturnsNull() {
            assertNull(CategoryMapper.fromCategoryToCategoryDto(null));
        }

        @Test
        void fromCategoryToCategoryDto_ShouldMapCorrectly() {
            Category category = Instancio.of(Category.class).withSeed(10).create();

            CategoryDto categoryDto = CategoryMapper.fromCategoryToCategoryDto(category);

            assertNotNull(categoryDto);
            assertAll(
                    () -> assertEquals(category.getId(), categoryDto.id()),
                    () -> assertEquals(category.getName(), categoryDto.name())
            );
        }
    }

    @Nested
    class fromCategoryDtoToCategoryTests {

        @Test
        void fromCategoryDtoToCategory_NullInput_ReturnsNull() {
            assertNull(CategoryMapper.fromCategoryDtoToCategory(null));
        }

        @Test
        void fromCategoryDtoToCategory_ShouldMapCorrectly() {
            CategoryDto categoryDto = Instancio.of(CategoryDto.class).withSeed(10).create();

            Category category = CategoryMapper.fromCategoryDtoToCategory(categoryDto);

            assertNotNull(categoryDto);
            assertAll(
                    () -> assertEquals(category.getId(), categoryDto.id()),
                    () -> assertEquals(category.getName(), categoryDto.name())
            );
        }
    }
}