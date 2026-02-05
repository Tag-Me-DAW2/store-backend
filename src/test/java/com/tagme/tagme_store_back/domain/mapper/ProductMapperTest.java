package com.tagme.tagme_store_back.domain.mapper;

import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.model.Product;
import org.instancio.Instancio;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

import static org.instancio.Select.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {
    @Nested
    class fromProductToProductDtoTests {
        @Test
        void fromProductToProductDto_NullInput_ReturnsNull() {
            assertNull(ProductMapper.fromProductToProductDto(null));
        }

        @Test
        void fromProductToProductDto_ShouldMapCorrectly() throws SQLException {
            byte[] imageData = "test image data".getBytes();
            Blob testBlob = new SerialBlob(imageData);

            Product product = Instancio.of(Product.class)
                    .set(field(Product::getImage), testBlob)
                    .withSeed(10)
                    .create();

            ProductDto productDto = ProductMapper.fromProductToProductDto(product);

            assertNotNull(productDto);
            assertAll(
                    () -> assertEquals(product.getId(), productDto.id()),
                    () -> assertEquals(product.getName(), productDto.name()),
                    () -> assertEquals(product.getDescription(), productDto.description()),
                    () -> assertEquals(product.getBasePrice(), productDto.basePrice()),
                    () -> assertEquals(product.getDiscountPercentage(), productDto.discountPercentage()),
                    () -> assertEquals(product.getPrice(), productDto.price()),
                    () -> assertArrayEquals(product.getImage().getBytes(1, (int) product.getImage().length()), productDto.image().getBytes(1, (int) productDto.image().length())),
                    () -> assertEquals(product.getCategory().getId(), productDto.category().id()),
                    () -> assertEquals(product.getCategory().getName(), productDto.category().name())
            );
        }
    }

    @Nested
    class fromProductDtoToProductTests {
        @Test
        void fromProductDtoToProduct_NullInput_ReturnsNull() {
            assertNull(ProductMapper.fromProductDtoToProduct(null));
        }

        @Test
        void fromProductDtoToProduct_ShouldMapCorrectly() throws SQLException {
            byte[] imageData = "test image data".getBytes();
            Blob testBlob = new SerialBlob(imageData);

            ProductDto productDto = Instancio.of(ProductDto.class)
                    .set(field(ProductDto::image), testBlob)
                    .withSeed(10)
                    .create();

            Product product = ProductMapper.fromProductDtoToProduct(productDto);

            assertNotNull(product);
            assertAll(
                    () -> assertEquals(productDto.id(), product.getId()),
                    () -> assertEquals(productDto.name(), product.getName()),
                    () -> assertEquals(productDto.description(), product.getDescription()),
                    () -> assertEquals(productDto.basePrice(), product.getBasePrice()),
                    () -> assertEquals(productDto.discountPercentage(), product.getDiscountPercentage()),
                    () -> assertArrayEquals(productDto.image().getBytes(1, (int) productDto.image().length()), product.getImage().getBytes(1, (int) product.getImage().length())),
                    () -> assertEquals(productDto.category().id(), product.getCategory().getId()),
                    () -> assertEquals(productDto.category().name(), product.getCategory().getName())
            );
        }
    }
}