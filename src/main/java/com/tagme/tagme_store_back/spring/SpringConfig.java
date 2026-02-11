package com.tagme.tagme_store_back.spring;

import com.tagme.tagme_store_back.EpsteinFiles.payment.CreditCardPaymentService;
import com.tagme.tagme_store_back.EpsteinFiles.payment.impl.CreditCardPaymentServiceImpl;
import com.tagme.tagme_store_back.domain.repository.AuthRepository;
import com.tagme.tagme_store_back.domain.repository.CategoryRepository;
import com.tagme.tagme_store_back.domain.repository.OrderRepository;
import com.tagme.tagme_store_back.domain.repository.PaymentInfoRepository;
import com.tagme.tagme_store_back.domain.repository.ProductRepository;
import com.tagme.tagme_store_back.domain.repository.UserRepository;
import com.tagme.tagme_store_back.domain.service.AuthService;
import com.tagme.tagme_store_back.domain.service.CartService;
import com.tagme.tagme_store_back.domain.service.CategoryService;
import com.tagme.tagme_store_back.domain.service.OrderService;
import com.tagme.tagme_store_back.domain.service.ProductService;
import com.tagme.tagme_store_back.domain.service.UserService;
import com.tagme.tagme_store_back.domain.service.impl.AuthServiceImpl;
import com.tagme.tagme_store_back.domain.service.impl.CartServiceImpl;
import com.tagme.tagme_store_back.domain.service.impl.CategoryServiceImpl;
import com.tagme.tagme_store_back.domain.service.impl.OrderServiceImpl;
import com.tagme.tagme_store_back.domain.service.impl.ProductServiceImpl;
import com.tagme.tagme_store_back.domain.service.impl.UserServiceImpl;
import com.tagme.tagme_store_back.persistence.dao.jpa.AuthJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.CategoryJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.OrderJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.PaymentInfoJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.ProductJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.UserJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.impl.AuthJpaDaoImpl;
import com.tagme.tagme_store_back.persistence.dao.jpa.impl.CategoryJpaDaoImpl;
import com.tagme.tagme_store_back.persistence.dao.jpa.impl.OrderJpaDaoImpl;
import com.tagme.tagme_store_back.persistence.dao.jpa.impl.PaymentInfoJpaDaoImpl;
import com.tagme.tagme_store_back.persistence.dao.jpa.impl.ProductJpaDaoImpl;
import com.tagme.tagme_store_back.persistence.dao.jpa.impl.UserJpaDaoImpl;
import com.tagme.tagme_store_back.persistence.repository.AuthRepositoryImpl;
import com.tagme.tagme_store_back.persistence.repository.CategoryRepositoryImpl;
import com.tagme.tagme_store_back.persistence.repository.OrderRepositoryImpl;
import com.tagme.tagme_store_back.persistence.repository.PaymentInfoRepositoryImpl;
import com.tagme.tagme_store_back.persistence.repository.ProductRepositoryImpl;
import com.tagme.tagme_store_back.persistence.repository.UserRepositoryImpl;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.unit.DataSize;

@Configuration
@EnableJpaRepositories(basePackages = "com.tagme.tagme_store_back.persistence.dao.jpa")
@EntityScan(basePackages = "com.tagme.tagme_store_back.persistence.dao.jpa.entity")
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
        return new AuthRepositoryImpl(authJpaDao);
    }

    @Bean
    public AuthService authService(AuthRepository authRepository, UserRepository userRepository) {
        return new AuthServiceImpl(authRepository, userRepository);
    }

    @Bean
    public CategoryJpaDao categoryJpaDao() {
        return new CategoryJpaDaoImpl();
    }

    @Bean
    public CategoryRepository categoryRepository(CategoryJpaDao categoryJpaDao) {
        return new CategoryRepositoryImpl(categoryJpaDao);
    }

    @Bean
    public CategoryService categoryService(CategoryRepository categoryRepository) {
        return new CategoryServiceImpl(categoryRepository);
    }

    @Bean
    public ProductJpaDao productJpaDao() {
        return new ProductJpaDaoImpl();
    }

    @Bean
    public ProductRepository productRepository(ProductJpaDao productJpaDao) {
        return new ProductRepositoryImpl(productJpaDao);
    }

    @Bean
    public ProductService productService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        return new ProductServiceImpl(productRepository, categoryRepository);
    }

    @Bean
    public OrderJpaDao orderJpaDao() {
        return new OrderJpaDaoImpl();
    }

    @Bean
    public OrderRepository orderRepository(OrderJpaDao orderJpaDao) {
        return new OrderRepositoryImpl(orderJpaDao);
    }

    @Bean
    public PaymentInfoJpaDao paymentInfoJpaDao() {
        return new PaymentInfoJpaDaoImpl();
    }

    @Bean
    public PaymentInfoRepository paymentInfoRepository(PaymentInfoJpaDao paymentInfoJpaDao, OrderJpaDao orderJpaDao) {
        return new PaymentInfoRepositoryImpl(paymentInfoJpaDao, orderJpaDao);
    }

    @Bean
    public CartService cartService(OrderRepository orderRepository, UserService userService, ProductService productService, CreditCardPaymentService creditCardPaymentService, PaymentInfoRepository paymentInfoRepository) {
        return new CartServiceImpl(orderRepository, userService, productService, creditCardPaymentService, paymentInfoRepository);
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepository, UserService userService, CreditCardPaymentService creditCardPaymentService, PaymentInfoRepository paymentInfoRepository) {
        return new OrderServiceImpl(orderRepository, userService, creditCardPaymentService, paymentInfoRepository);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(50));
        factory.setMaxRequestSize(DataSize.ofMegabytes(50));
        return factory.createMultipartConfig();
    }
}
