package com.tagme.tagme_store_back.controller;

import com.tagme.tagme_store_back.controller.mapper.ProductMapper;
import com.tagme.tagme_store_back.controller.webModel.request.ProductInsertRequest;
import com.tagme.tagme_store_back.controller.webModel.request.ProductUpdateRequest;
import com.tagme.tagme_store_back.controller.webModel.response.ProductDetailResponse;
import com.tagme.tagme_store_back.controller.webModel.response.ProductSummaryResponse;
import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.service.ProductService;
import com.tagme.tagme_store_back.domain.validation.DtoValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductSummaryResponse>> getAllProducts(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        Page<ProductDto> productDtoPage = productService.getAll(page, size);

        List<ProductSummaryResponse> productsList = productDtoPage.data().stream()
                .map(ProductMapper::fromProductDtoToProductSummaryResponse).toList();

        Page<ProductSummaryResponse> responsePage = new Page<>(productsList, page, size, productDtoPage.totalElements(), productDtoPage.totalPages());

        return new ResponseEntity<>(responsePage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProductById(@PathVariable Long id) {
        ProductDetailResponse product = ProductMapper.fromProductDtoToProductDetailResponse(productService.getById(id));

        return new ResponseEntity<>(product, HttpStatus.OK);
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

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalProducts() {
        Long totalProducts = productService.getTotalProducts();
        return new ResponseEntity<>(totalProducts, HttpStatus.OK);
    }
}
