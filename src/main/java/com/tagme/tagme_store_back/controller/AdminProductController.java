package com.tagme.tagme_store_back.controller;

import com.tagme.tagme_store_back.controller.mapper.ProductMapper;
import com.tagme.tagme_store_back.controller.webModel.request.ProductInsertRequest;
import com.tagme.tagme_store_back.controller.webModel.request.ProductUpdateRequest;
import com.tagme.tagme_store_back.controller.webModel.response.ProductDetailResponse;
import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.service.ProductService;
import com.tagme.tagme_store_back.domain.validation.DtoValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {
    private ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDetailResponse> createProduct(@RequestBody ProductInsertRequest productInsertRequest) throws SQLException, IOException {
        ProductDto productDto = ProductMapper.fromProductInsertRequestToProductDto(productInsertRequest);
        DtoValidator.validate(productInsertRequest);

        ProductDetailResponse createdProduct = ProductMapper.fromProductDtoToProductDetailResponse(productService.create(productDto));

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateRequest productUpdateRequest) throws SQLException, IOException {
        if (!id.equals(productUpdateRequest.id())) {
            throw new BusinessException("Path variable ID does not match request body ID");
        }
        ProductDto productDto = ProductMapper.fromProductUpdateRequestToProductDto(productUpdateRequest);
        DtoValidator.validate(productDto);

        ProductDetailResponse updatedProduct = ProductMapper.fromProductDtoToProductDetailResponse(productService.update(productDto));

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
