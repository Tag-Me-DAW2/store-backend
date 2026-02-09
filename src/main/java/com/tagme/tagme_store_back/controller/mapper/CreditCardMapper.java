package com.tagme.tagme_store_back.controller.mapper;

import com.tagme.tagme_store_back.controller.webModel.request.CreditCardRequest;
import com.tagme.tagme_store_back.domain.dto.CreditCardDto;

public class CreditCardMapper {
    public static CreditCardDto toDto(CreditCardRequest request) {
        if (request == null) {
            return null;
        }

        return new CreditCardDto(
                request.cardNumber(),
                request.cardHolderName(),
                request.cvv(),
                request.expirationDate()
        );
    }
}
