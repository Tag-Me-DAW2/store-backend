package com.tagme.tagme_store_back.controller.mapper;

import com.tagme.tagme_store_back.controller.webModel.request.OrderItemRequest;
import com.tagme.tagme_store_back.controller.webModel.request.OrderUpdateRequest;
import com.tagme.tagme_store_back.controller.webModel.response.OrderItemResponse;
import com.tagme.tagme_store_back.controller.webModel.response.OrderResponse;
import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.OrderItemDto;
import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.model.OrderStatus;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

public class OrderMapper {

    public static OrderResponse fromOrderDtoToOrderResponse(OrderDto orderDto) throws IOException {
        if (orderDto == null) {
            return null;
        }

        List<OrderItemResponse> itemResponses = null;
        if (orderDto.orderItems() != null) {
            itemResponses = orderDto.orderItems().stream()
                    .map(item -> {
                        try {
                            return fromOrderItemDtoToOrderItemResponse(item);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        }

        return new OrderResponse(
                orderDto.id(),
                orderDto.user() != null ? orderDto.user().id() : null,
                orderDto.orderStatus(),
                itemResponses,
                orderDto.totalPrice(),
                orderDto.paidDate()
        );
    }

    public static OrderItemResponse fromOrderItemDtoToOrderItemResponse(OrderItemDto orderItemDto) throws IOException {
        if (orderItemDto == null) {
            return null;
        }

        return new OrderItemResponse(
                orderItemDto.id(),
                ProductMapper.fromProductDtoToProductSummaryResponse(orderItemDto.productDto()),
                orderItemDto.productName(),
                blobToBase64(orderItemDto.productImage()),
                orderItemDto.productImageName(),
                orderItemDto.quantity(),
                orderItemDto.basePrice(),
                orderItemDto.discountPercentage(),
                orderItemDto.total()
        );
    }

    private static String blobToBase64(Blob blob) {
        if (blob == null) {
            return null;
        }
        try {
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (SQLException e) {
            return null;
        }
    }

    public static OrderDto fromOrderUpdateRequestToOrderDto(OrderUpdateRequest request, UserDto user) {
        if (request == null) {
            return null;
        }

        List<OrderItemDto> orderItems = null;
        if (request.orderItems() != null) {
            orderItems = request.orderItems().stream()
                    .map(OrderMapper::fromOrderItemRequestToOrderItemDto)
                    .toList();
        }

        return new OrderDto(
                null,
                user,
                OrderStatus.PENDING,
                orderItems,
                null,
                null,
                null
        );
    }

    public static OrderItemDto fromOrderItemRequestToOrderItemDto(OrderItemRequest itemRequest) {
        if (itemRequest == null) {
            return null;
        }

        // Se crea un ProductDto solo con el ID para que el servicio lo resuelva
        ProductDto productDto = new ProductDto(
                itemRequest.productId(),
                null, null, null, null, null, null, null, null, null
        );

        return new OrderItemDto(
                null,
                productDto,
                null, // productName - se resolverá en el servicio
                null, // productImage - se resolverá en el servicio
                null, // productImageName - se resolverá en el servicio
                itemRequest.quantity(),
                null, // basePrice - se resolverá en el servicio
                null, // discountPercentage - se resolverá en el servicio
                null  // total - se calculará en el servicio
        );
    }
}

