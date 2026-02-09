package com.tagme.tagme_store_back.domain.mapper;

import com.tagme.tagme_store_back.domain.dto.ShippingInfoDto;
import com.tagme.tagme_store_back.domain.model.ShippingInfo;

public class ShippingInfoMapper {
    public static ShippingInfo fromShippingInfoDtoToShippingInfo(ShippingInfoDto shippingInfoDto) {
        if (shippingInfoDto == null) {
            return null;
        }
        return new ShippingInfo(
                shippingInfoDto.id(),
                shippingInfoDto.orderId(),
                shippingInfoDto.firstName(),
                shippingInfoDto.lastName(),
                shippingInfoDto.email(),
                shippingInfoDto.address(),
                shippingInfoDto.city(),
                shippingInfoDto.postalCode(),
                shippingInfoDto.country(),
                shippingInfoDto.createdAt()
        );
    }

    public static ShippingInfoDto fromShippingInfoToShippingInfoDto(ShippingInfo shippingInfo) {
        if (shippingInfo == null) {
            return null;
        }
        return new ShippingInfoDto(
                shippingInfo.getId(),
                shippingInfo.getOrderId(),
                shippingInfo.getFirstName(),
                shippingInfo.getLastName(),
                shippingInfo.getEmail(),
                shippingInfo.getAddress(),
                shippingInfo.getCity(),
                shippingInfo.getPostalCode(),
                shippingInfo.getCountry(),
                shippingInfo.getCreatedAt()
        );
    }
}
