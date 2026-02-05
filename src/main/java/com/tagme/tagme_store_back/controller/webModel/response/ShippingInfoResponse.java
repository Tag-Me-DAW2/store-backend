package com.tagme.tagme_store_back.controller.webModel.response;

public record ShippingInfoResponse(
        Long id,
        String name,
        String surname,
        String email,
        String address,
        String city,
        String postalCode,
        String country
) {
}
