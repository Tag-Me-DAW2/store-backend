package com.tagme.tagme_store_back.persistence.mapper;

import com.tagme.tagme_store_back.domain.dto.OrderItemDto;
import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderItemJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.ProductJpaEntity;

import java.sql.Blob;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;

public class OrderItemMapper {

    /**
     * Convierte OrderItemDto a entidad JPA.
     * Siempre guarda el snapshot del producto para preservar datos históricos.
     */
    public static OrderItemJpaEntity toJpaEntity(OrderItemDto orderItemDto) {
        if (orderItemDto == null) {
            return null;
        }

        ProductJpaEntity productEntity = ProductMapper.fromProductDtoToProductJpaEntity(orderItemDto.productDto());
        ProductDto productDto = orderItemDto.productDto();

        // Snapshot: usar datos del DTO si están disponibles, sino del producto
        String snapshotName = orderItemDto.productName() != null ? 
                orderItemDto.productName() : 
                (productEntity != null ? productEntity.getName() : null);
        
        byte[] snapshotImage = blobToByteArray(orderItemDto.productImage());
        if (snapshotImage == null && productEntity != null) {
            snapshotImage = productEntity.getImage();
        }
        
        String snapshotImageName = orderItemDto.productImageName() != null ? 
                orderItemDto.productImageName() : 
                (productEntity != null ? productEntity.getImageName() : null);

        return new OrderItemJpaEntity(
                orderItemDto.id(),
                null, // order se setea después
                productEntity,
                snapshotName,
                snapshotImage,
                snapshotImageName,
                orderItemDto.basePrice() != null ? orderItemDto.basePrice() :
                        (productEntity != null ? productEntity.getBasePrice() : null),
                orderItemDto.discountPercentage() != null ? orderItemDto.discountPercentage() :
                        (productEntity != null ? productEntity.getDiscountPercentage() : null),
                orderItemDto.quantity()
        );
    }

    /**
     * Convierte entidad JPA a OrderItemDto según el estado del pedido:
     * - PENDING: Los datos se obtienen del Product (carrito activo)
     * - PAYED/PROCESSING: Los datos se obtienen del snapshot guardado
     */
    public static OrderItemDto fromJpaEntity(OrderItemJpaEntity entity, OrderStatus orderStatus) {
        if (entity == null) {
            return null;
        }

        ProductDto productDto = ProductMapper.fromProductJpaEntityToProductDto(entity.getProduct());
        ProductJpaEntity product = entity.getProduct();

        if (orderStatus == OrderStatus.PENDING && product != null) {
            // Carrito: usar datos actuales del producto
            return new OrderItemDto(
                    entity.getId(),
                    productDto,
                    product.getName(),
                    byteArrayToBlob(product.getImage()),
                    product.getImageName(),
                    entity.getQuantity(),
                    product.getBasePrice(),
                    product.getDiscountPercentage(),
                    null // total se calcula en el dominio
            );
        } else {
            // Pedido pagado/procesando: usar datos del snapshot
            return new OrderItemDto(
                    entity.getId(),
                    productDto,
                    entity.getProductName(),
                    byteArrayToBlob(entity.getProductImage()),
                    entity.getProductImageName(),
                    entity.getQuantity(),
                    entity.getBasePrice(),
                    entity.getDiscountPercentage(),
                    null // total se calcula en el dominio
            );
        }
    }

    /**
     * Versión sin estado - usa los datos del snapshot por defecto
     */
    public static OrderItemDto fromJpaEntity(OrderItemJpaEntity entity) {
        return fromJpaEntity(entity, OrderStatus.PAYED);
    }

    private static byte[] blobToByteArray(Blob blob) {
        if (blob == null) {
            return null;
        }
        try {
            return blob.getBytes(1, (int) blob.length());
        } catch (SQLException e) {
            return null;
        }
    }

    private static Blob byteArrayToBlob(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            return new SerialBlob(bytes);
        } catch (SQLException e) {
            return null;
        }
    }
}
