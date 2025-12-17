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
@CrossOrigin("*")
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
}
