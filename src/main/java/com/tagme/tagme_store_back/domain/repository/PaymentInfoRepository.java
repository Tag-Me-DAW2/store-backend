package com.tagme.tagme_store_back.domain.repository;

import com.tagme.tagme_store_back.domain.dto.PaymentInfoDto;

import java.util.Optional;

public interface PaymentInfoRepository {
    PaymentInfoDto save(PaymentInfoDto paymentInfoDto);
    Optional<PaymentInfoDto> findByOrderId(Long orderId);
    void deleteByOrderId(Long orderId);
}
