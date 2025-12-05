package com.tagme.tagme_store_back.persistence;

import com.tagme.tagme_store_back.domain.repository.AuthRepository;
import com.tagme.tagme_store_back.domain.repository.UserRepository;
import com.tagme.tagme_store_back.domain.service.AuthService;
import com.tagme.tagme_store_back.domain.service.UserService;
import com.tagme.tagme_store_back.domain.service.impl.AuthServiceImpl;
import com.tagme.tagme_store_back.domain.service.impl.UserServiceImpl;
import com.tagme.tagme_store_back.persistence.dao.jpa.AuthJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.UserJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.impl.AuthJpaDaoImpl;
import com.tagme.tagme_store_back.persistence.dao.jpa.impl.UserJpaDaoImpl;
import com.tagme.tagme_store_back.persistence.repository.AuthRepositoryImpl;
import com.tagme.tagme_store_back.persistence.repository.UserRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

//@Configuration
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
}
