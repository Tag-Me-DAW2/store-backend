package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.domain.dto.PaymentInfoDto;
import com.tagme.tagme_store_back.domain.repository.PaymentInfoRepository;
import com.tagme.tagme_store_back.persistence.dao.jpa.OrderJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.PaymentInfoJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.PaymentInfoJpaEntity;

import java.util.Optional;

public class PaymentInfoRepositoryImpl implements PaymentInfoRepository {
    
    private final PaymentInfoJpaDao paymentInfoJpaDao;
    private final OrderJpaDao orderJpaDao;

    public PaymentInfoRepositoryImpl(PaymentInfoJpaDao paymentInfoJpaDao, OrderJpaDao orderJpaDao) {
        this.paymentInfoJpaDao = paymentInfoJpaDao;
        this.orderJpaDao = orderJpaDao;
    }

    @Override
    public PaymentInfoDto save(PaymentInfoDto paymentInfoDto) {
        if (paymentInfoDto == null) {
            throw new IllegalArgumentException("El DTO de PaymentInfo no puede ser nulo");
        }

        OrderJpaEntity order = orderJpaDao.findById(paymentInfoDto.orderId())
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + paymentInfoDto.orderId()));

        PaymentInfoJpaEntity entity = new PaymentInfoJpaEntity(
                paymentInfoDto.id(),
                order,
                paymentInfoDto.cardNumber(),
                paymentInfoDto.cardHolderName(),
                paymentInfoDto.cvv(),
                paymentInfoDto.expirationDate()
        );

        PaymentInfoJpaEntity saved = paymentInfoJpaDao.save(entity);

        return new PaymentInfoDto(
                saved.getId(),
                saved.getOrder().getId(),
                saved.getCardNumber(),
                saved.getCardHolderName(),
                saved.getCvv(),
                saved.getExpirationDate()
        );
    }

    @Override
    public Optional<PaymentInfoDto> findByOrderId(Long orderId) {
        return paymentInfoJpaDao.findByOrderId(orderId)
                .map(entity -> new PaymentInfoDto(
                        entity.getId(),
                        entity.getOrder().getId(),
                        entity.getCardNumber(),
                        entity.getCardHolderName(),
                        entity.getCvv(),
                        entity.getExpirationDate()
                ));
    }

    @Override
    public void deleteByOrderId(Long orderId) {
        paymentInfoJpaDao.deleteByOrderId(orderId);
    }
}
