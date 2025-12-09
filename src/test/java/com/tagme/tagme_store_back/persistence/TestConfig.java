package com.tagme.tagme_store_back.persistence;

import com.tagme.tagme_store_back.persistence.dao.jpa.AuthJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.ProductJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.UserJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.impl.AuthJpaDaoImpl;
import com.tagme.tagme_store_back.persistence.dao.jpa.impl.ProductJpaDaoImpl;
import com.tagme.tagme_store_back.persistence.dao.jpa.impl.UserJpaDaoImpl;
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
    public ProductJpaDao productJpaDao() {
        return new ProductJpaDaoImpl();
    }
}
