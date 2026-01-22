package com.tagme.tagme_store_back.controller;

import com.tagme.tagme_store_back.controller.mapper.ProductMapper;
import com.tagme.tagme_store_back.controller.webModel.request.ProductInsertRequest;
import com.tagme.tagme_store_back.controller.webModel.request.ProductUpdateRequest;
import com.tagme.tagme_store_back.controller.webModel.response.ProductDetailResponse;
import com.tagme.tagme_store_back.controller.webModel.response.ProductSummaryResponse;
import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.model.ProductSort;
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
@CrossOrigin("*")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductSummaryResponse>> getAllProducts(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int size){
        Page<ProductDto> productDtoPage = productService.getAll(page, size);

        List<ProductSummaryResponse> productsList = productDtoPage.data().stream()
                .map(dto -> {
            try {
                return ProductMapper.fromProductDtoToProductSummaryResponse(dto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        Page<ProductSummaryResponse> responsePage = new Page<>(productsList, page, size, productDtoPage.totalElements(), productDtoPage.totalPages());

        return new ResponseEntity<>(responsePage, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ProductSummaryResponse>> getFilteredProducts(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String material,
            @RequestParam(required = false, defaultValue = "0") Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false, defaultValue = "MOST_POPULAR") ProductSort sort
    ) {
        Page<ProductDto> productDtoPage = productService.getFilteredProducts(page, size, name, categoryId, material, minPrice, maxPrice, sort);

        List<ProductSummaryResponse> productsList = productDtoPage.data().stream()
                .map(dto -> {
                    try {
                        return ProductMapper.fromProductDtoToProductSummaryResponse(dto);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        Page<ProductSummaryResponse> responsePage = new Page<>(productsList, page, size, productDtoPage.totalElements(), productDtoPage.totalPages());

        return new ResponseEntity<>(responsePage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProductById(@PathVariable Long id) throws IOException {
        ProductDetailResponse product = ProductMapper.fromProductDtoToProductDetailResponse(productService.getById(id));

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalProducts() {
        Long totalProducts = productService.getTotalProducts();
        return new ResponseEntity<>(totalProducts, HttpStatus.OK);
    }
}
