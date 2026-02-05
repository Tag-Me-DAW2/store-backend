package com.tagme.tagme_store_back.web.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;

import java.io.IOException;

@Order(1)
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String path = httpServletRequest.getRequestURI();

        if (path.equals("/auth/login")) {
            request.getRequestDispatcher("/auth/login").forward(request, response);
            return;
        }

        chain.doFilter(request, response);
    }
}
