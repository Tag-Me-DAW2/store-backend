package com.tagme.tagme_store_back.controller.webModel.request;

public record ShippingInfoRequest(
        String firstName,
        String lastName,
        String email,
        String phone,
        String address,
        String city,
        String postalCode,
        String country
) {
}
