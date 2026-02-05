package com.tagme.tagme_store_back.domain.mapper;

import com.tagme.tagme_store_back.domain.dto.OrderItemDto;
import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.model.OrderItem;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.model.Product;

import java.sql.Blob;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;

public class OrderItemMapper {
    
    public static OrderItem fromOrderItemDtoToOrderItem(OrderItemDto orderItemDto) {
        if (orderItemDto == null) {
            return null;
        }

        Product product = ProductMapper.fromProductDtoToProduct(orderItemDto.productDto());
        
        return new OrderItem(
                orderItemDto.id(),
                product,
                orderItemDto.productName(),
                blobToByteArray(orderItemDto.productImage()),
                orderItemDto.productImageName(),
                orderItemDto.quantity(),
                orderItemDto.basePrice(),
                orderItemDto.discountPercentage()
        );
    }

    /**
     * Mapea OrderItem a OrderItemDto según el estado del pedido:
     * - PENDING: Los datos se obtienen del Product (carrito activo)
     * - PAYED/PROCESSING: Los datos se obtienen del snapshot guardado en OrderItem
     */
    public static OrderItemDto fromOrderItemToOrderItemDto(OrderItem orderItem, OrderStatus orderStatus) {
        if (orderItem == null) {
            return null;
        }

        ProductDto productDto = ProductMapper.fromProductToProductDto(orderItem.getProduct());
        
        if (orderStatus == OrderStatus.PENDING && orderItem.getProduct() != null) {
            // Carrito: usar datos actuales del producto
            Product product = orderItem.getProduct();
            return new OrderItemDto(
                    orderItem.getId(),
                    productDto,
                    product.getName(),
                    product.getImage(),
                    product.getImageName(),
                    orderItem.getQuantity(),
                    product.getBasePrice(),
                    product.getDiscountPercentage(),
                    orderItem.getTotal()
            );
        } else {
            // Pedido pagado: usar datos del snapshot
            return new OrderItemDto(
                    orderItem.getId(),
                    productDto,
                    orderItem.getProductName(),
                    byteArrayToBlob(orderItem.getProductImage()),
                    orderItem.getProductImageName(),
                    orderItem.getQuantity(),
                    orderItem.getBasePrice(),
                    orderItem.getDiscountPercentage(),
                    orderItem.getTotal()
            );
        }
    }

    /**
     * Versión sin estado - usa los datos del snapshot por defecto
     */
    public static OrderItemDto fromOrderItemToOrderItemDto(OrderItem orderItem) {
        return fromOrderItemToOrderItemDto(orderItem, OrderStatus.PAYED);
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
