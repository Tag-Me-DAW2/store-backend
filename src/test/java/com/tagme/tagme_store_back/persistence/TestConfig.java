package com.tagme.tagme_store_back.persistence;

import com.tagme.tagme_store_back.persistence.dao.jpa.*;
import com.tagme.tagme_store_back.persistence.dao.jpa.impl.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@TestConfiguration
@EnableJpaRepositories(basePackages = "com.tagme.tagme_store_back.persistence.dao.jpa")
@EntityScan(basePackages = "com.tagme.tagme_store_back.persistence.dao.jpa.entity")
public class TestConfig {

    @Bean
    public UserJpaDao userJpaDao() {
        return new UserJpaDaoImpl();
    }

    @Bean
    public AuthJpaDao authJpaDao() {
        return new AuthJpaDaoImpl();
    }

    @Bean
    public CategoryJpaDao categoryJpaDao() {
        return new CategoryJpaDaoImpl();
    }

    @Bean
    public ProductJpaDao productJpaDao() {
        return new ProductJpaDaoImpl();
    }

    @Bean
    public OrderJpaDao orderJpaDao() {
        return new OrderJpaDaoImpl();
    }

    @Bean
    public PaymentInfoJpaDao paymentInfoJpaDao() {
        return new PaymentInfoJpaDaoImpl();
    }
}
