package com.tagme.tagme_store_back.controller.mapper;

import com.tagme.tagme_store_back.controller.webModel.request.CategoryRequest;
import com.tagme.tagme_store_back.controller.webModel.response.CategoryResponse;
import com.tagme.tagme_store_back.domain.dto.CategoryDto;

public class CategoryMapper {
    public static CategoryResponse fromCategoryDtoToCategoryResponse(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }
        return new CategoryResponse(
                categoryDto.id(),
                categoryDto.name()
        );
    }

    public static CategoryDto fromCategoryResponseToCategoryDto(CategoryResponse categoryResponse) {
        if (categoryResponse == null) {
            return null;
        }
        return new CategoryDto(
                categoryResponse.id(),
                categoryResponse.name()
        );
    }

    public static CategoryDto fromCategoryRequestToCategoryDto(CategoryRequest categoryInsertRequest) {
        if (categoryInsertRequest == null) {
            return null;
        }
        return new CategoryDto(
                categoryInsertRequest.id(),
                categoryInsertRequest.name()
        );
    }
}
