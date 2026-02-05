package com.tagme.tagme_store_back.controller.webModel.request;

public record ShippingInfoRequest(
        String name,
        String surname,
        String email,
        String address,
        String city,
        String postalCode,
        String country
) {
}
