package com.tagme.tagme_store_back.web.filters;

import com.tagme.tagme_store_back.controller.mapper.UserMapper;
import com.tagme.tagme_store_back.controller.webModel.UserResponse;
import com.tagme.tagme_store_back.domain.service.UserService;
import com.tagme.tagme_store_back.web.context.AuthContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Order(1)
public class AuhtFilter implements Filter {

    @Autowired
    private UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String token = authHeader.substring(7);
        UserResponse userResponse = UserMapper.fromUserDtoToUserResponse(userService.getByToken(token));

        if(userResponse != null) {
            AuthContext.setUser(userResponse);
            filterChain.doFilter(servletRequest, servletResponse);
            AuthContext.clear();
        } else {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }


}
