package com.tagme.tagme_store_back.web.filters;

import com.tagme.tagme_store_back.controller.webModel.UserResponse;
import com.tagme.tagme_store_back.domain.model.UserRole;
import com.tagme.tagme_store_back.web.context.AuthContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(2)
public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String path = httpServletRequest.getRequestURI();
        if (path.startsWith("/admin")) {
            UserResponse user = AuthContext.getUser();

            if (user == null) {
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            if (!user.role().equals(UserRole.ADMIN)) {
                httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
