package com.tagme.tagme_store_back.persistence.mapper;

import com.tagme.tagme_store_back.domain.dto.ShippingInfoDto;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.ShippingInfoJpaEntity;

import java.time.LocalDateTime;

public class ShippingInfoMapper {
    public static ShippingInfoJpaEntity toJpaEntity(ShippingInfoDto shippingInfoDto, OrderJpaEntity order) {
        if (shippingInfoDto == null) {
            return null;
        }
        ShippingInfoJpaEntity entity = new ShippingInfoJpaEntity(
                shippingInfoDto.id(),
                order,
                shippingInfoDto.firstName(),
                shippingInfoDto.lastName(),
                shippingInfoDto.email(),
                shippingInfoDto.address(),
                shippingInfoDto.city(),
                shippingInfoDto.postalCode(),
                shippingInfoDto.country()
        );
        entity.setCreatedAt(shippingInfoDto.createdAt() != null ? shippingInfoDto.createdAt() : LocalDateTime.now());
        return entity;
    }

    public static ShippingInfoDto fromJpaEntity(ShippingInfoJpaEntity shippingInfoJpaEntity) {
        if (shippingInfoJpaEntity == null) {
            return null;
        }
        return new ShippingInfoDto(
                shippingInfoJpaEntity.getId(),
                shippingInfoJpaEntity.getOrder() != null ? shippingInfoJpaEntity.getOrder().getId() : null,
                shippingInfoJpaEntity.getFirstName(),
                shippingInfoJpaEntity.getLastName(),
                shippingInfoJpaEntity.getEmail(),
                shippingInfoJpaEntity.getAddress(),
                shippingInfoJpaEntity.getCity(),
                shippingInfoJpaEntity.getPostalCode(),
                shippingInfoJpaEntity.getCountry(),
                shippingInfoJpaEntity.getCreatedAt()
        );
    }
}
