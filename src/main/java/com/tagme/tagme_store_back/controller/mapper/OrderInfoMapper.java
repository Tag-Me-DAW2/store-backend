package com.tagme.tagme_store_back.controller.mapper;

import com.tagme.tagme_store_back.controller.webModel.request.OrderInfoRequest;
import com.tagme.tagme_store_back.domain.dto.OrderInfoDto;

public class OrderInfoMapper {
    public static OrderInfoDto toDto(OrderInfoRequest request) {
        if (request == null) {
            return null;
        }

        return new OrderInfoDto(
                request.shippingCost(),
                request.subtotal(),
                request.total()
        );
    }
}
