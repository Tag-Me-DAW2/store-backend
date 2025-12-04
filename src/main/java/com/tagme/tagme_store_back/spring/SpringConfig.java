package com.tagme.tagme_store_back.spring;

import com.tagme.tagme_store_back.domain.repository.AuthRepository;
import com.tagme.tagme_store_back.domain.repository.UserRepository;
import com.tagme.tagme_store_back.domain.service.AuthService;
import com.tagme.tagme_store_back.domain.service.UserService;
import com.tagme.tagme_store_back.domain.service.impl.AuthServiceImpl;
import com.tagme.tagme_store_back.domain.service.impl.UserServiceImpl;
import com.tagme.tagme_store_back.persistence.dao.jpa.AuthJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.impl.AuthJpaDaoImpl;
import com.tagme.tagme_store_back.persistence.repository.AuthRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    @Bean
    public UserJpaDao userJpaDao() {
        return new UserJpaDaoImpl();
    }

    @Bean
    public UserRepository userRepository(UserJpaDao userJpaDao) {
        return new UserRepositoryImpl(userJpaDao);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public AuthJpaDao authJpaDao() {
        return new AuthJpaDaoImpl();
    }

    @Bean
    public AuthRepository authRepository(AuthJpaDao authJpaDao) {
        return new AuthRepositoryImpl(authJpaDao) {
        };
    }

    @Bean
    public AuthService authService(AuthRepository authRepository, UserRepository userRepository) {
        return new AuthServiceImpl(authRepository, userRepository);
    }
}
