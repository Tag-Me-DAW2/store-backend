package com.tagme.tagme_store_back.controller.mapper;

import com.tagme.tagme_store_back.controller.webModel.request.CartItemRequest;
import com.tagme.tagme_store_back.controller.webModel.request.CartUpdateRequest;
import com.tagme.tagme_store_back.controller.webModel.response.CartItemResponse;
import com.tagme.tagme_store_back.controller.webModel.response.CartResponse;
import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.OrderItemDto;
import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;

import java.io.IOException;
import java.util.List;

public class CartMapper {

    public static CartResponse fromOrderDtoToCartResponse(OrderDto orderDto) throws IOException {
        if (orderDto == null) {
            return null;
        }

        List<CartItemResponse> itemResponses = null;
        if (orderDto.orderItems() != null) {
            itemResponses = orderDto.orderItems().stream()
                    .map(item -> {
                        try {
                            return fromOrderItemDtoToCartItemResponse(item);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        }

        return new CartResponse(
                orderDto.id(),
                orderDto.user() != null ? orderDto.user().id() : null,
                orderDto.orderStatus(),
                itemResponses,
                orderDto.totalPrice(),
                orderDto.createdAt()
        );
    }

    public static CartItemResponse fromOrderItemDtoToCartItemResponse(OrderItemDto orderItemDto) throws IOException {
        if (orderItemDto == null) {
            return null;
        }

        return new CartItemResponse(
                orderItemDto.id(),
                ProductMapper.fromProductDtoToProductSummaryResponse(orderItemDto.productDto()),
                orderItemDto.quantity(),
                orderItemDto.basePrice(),
                orderItemDto.discountPercentage(),
                orderItemDto.total()
        );
    }

    public static OrderDto fromCartUpdateRequestToOrderDto(CartUpdateRequest request, UserDto user) {
        if (request == null) {
            return null;
        }

        List<OrderItemDto> orderItems = null;
        if (request.orderItems() != null) {
            orderItems = request.orderItems().stream()
                    .map(CartMapper::fromCartItemRequestToOrderItemDto)
                    .toList();
        }

        return new OrderDto(
                null,
                user,
                null,
                orderItems,
                null,
                null
        );
    }

    public static OrderItemDto fromCartItemRequestToOrderItemDto(CartItemRequest itemRequest) {
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
                itemRequest.quantity(),
                null,
                null,
                null
        );
    }
}

