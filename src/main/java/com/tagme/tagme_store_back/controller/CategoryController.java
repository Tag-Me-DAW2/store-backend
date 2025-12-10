package com.tagme.tagme_store_back.controller;

import com.tagme.tagme_store_back.controller.mapper.CategoryMapper;
import com.tagme.tagme_store_back.controller.webModel.request.CategoryRequest;
import com.tagme.tagme_store_back.controller.webModel.response.CategoryResponse;
import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.service.CategoryService;
import com.tagme.tagme_store_back.domain.validation.DtoValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAll().stream().map(CategoryMapper::fromCategoryDtoToCategoryResponse).toList();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        CategoryResponse categoryResponse = CategoryMapper.fromCategoryDtoToCategoryResponse(categoryService.getById(id));
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryInsertRequest) {
        CategoryDto categoryDto = categoryService.create(CategoryMapper.fromCategoryRequestToCategoryDto(categoryInsertRequest));
        DtoValidator.validate(categoryDto);

        CategoryResponse categoryResponse = CategoryMapper.fromCategoryDtoToCategoryResponse(categoryService.create(categoryDto));

        return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        if (!id.equals(categoryRequest.id())) {
            throw new BusinessException("ID in path and request body must match");
        }
        CategoryDto categoryDto = CategoryMapper.fromCategoryRequestToCategoryDto(categoryRequest);
        DtoValidator.validate(categoryDto);

        CategoryResponse categoryResponse = CategoryMapper.fromCategoryDtoToCategoryResponse(categoryService.update(categoryDto));

        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }
}
