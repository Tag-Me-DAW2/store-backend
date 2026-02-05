package com.tagme.tagme_store_back.controller.mapper;

import com.tagme.tagme_store_back.controller.webModel.request.ShippingInfoRequest;
import com.tagme.tagme_store_back.controller.webModel.response.ShippingInfoResponse;
import com.tagme.tagme_store_back.domain.dto.ShippingInfoDto;

public class ShippingInfoMapper {
    public static ShippingInfoResponse fromShippingInfoDtoToShippingInfoResponse(ShippingInfoDto shippingInfoDto) {
        if (shippingInfoDto == null) {
            return null;
        }
        return new ShippingInfoResponse(
                shippingInfoDto.id(),
                shippingInfoDto.name(),
                shippingInfoDto.surname(),
                shippingInfoDto.email(),
                shippingInfoDto.address(),
                shippingInfoDto.city(),
                shippingInfoDto.postalCode(),
                shippingInfoDto.country()
        );
    }

    public static ShippingInfoDto fromShippingInfoRequestToShippingInfoDto(ShippingInfoRequest request) {
        if (request == null) {
            return null;
        }
        return new ShippingInfoDto(
                null,
                null,
                request.name(),
                request.surname(),
                request.email(),
                request.address(),
                request.city(),
                request.postalCode(),
                request.country(),
                null
        );
    }
}
