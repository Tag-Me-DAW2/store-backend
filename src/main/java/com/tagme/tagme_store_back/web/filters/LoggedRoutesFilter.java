package com.tagme.tagme_store_back.web.filters;

import com.tagme.tagme_store_back.controller.webModel.response.UserResponse;
import com.tagme.tagme_store_back.web.context.AuthContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Order(3)
public class LoggedRoutesFilter implements Filter {
    private final List<String> protectedRoutes = List.of(
            "/admin"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String path = httpServletRequest.getRequestURI();

        boolean serveProtectedRoute = protectedRoutes.stream().anyMatch(path::startsWith);

        if (!serveProtectedRoute) {
            chain.doFilter(request, response);
            return;
        }

        UserResponse user = AuthContext.getUser();

        if (user == null) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        chain.doFilter(request, response);
    }
}
