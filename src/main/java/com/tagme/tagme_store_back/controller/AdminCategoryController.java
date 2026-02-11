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

@RestController
@RequestMapping("/admin/categories")
@CrossOrigin("*")
public class AdminCategoryController {
    private CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
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
            throw new BusinessException("El ID de la ruta no coincide con el ID del cuerpo de la petición");
        }
        CategoryDto categoryDto = CategoryMapper.fromCategoryRequestToCategoryDto(categoryRequest);
        DtoValidator.validate(categoryDto);

        CategoryResponse categoryResponse = CategoryMapper.fromCategoryDtoToCategoryResponse(categoryService.update(categoryDto));

        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }
}
