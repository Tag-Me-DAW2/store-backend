package com.tagme.tagme_store_back.controller.mapper;

import com.tagme.tagme_store_back.controller.webModel.request.PayCartRequest;
import com.tagme.tagme_store_back.domain.dto.PayCartDto;

public class PayCartMapper {
        public static PayCartDto toDto(PayCartRequest request) {
            if (request == null) {
                return null;
            }

            return new PayCartDto(
                    OrderInfoMapper.toDto(request.orderInfo()),
                    CreditCardMapper.toDto(request.paymentInfo()),
                    ShippingInfoMapper.fromShippingInfoRequestToShippingInfoDto(request.shippingInfo())
            );
        }
}
