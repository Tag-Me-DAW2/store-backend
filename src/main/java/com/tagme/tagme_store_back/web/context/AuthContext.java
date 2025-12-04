package com.tagme.tagme_store_back.web.context;

import com.tagme.tagme_store_back.controller.webModel.UserResponse;

public class AuthContext {
    private static final ThreadLocal<UserResponse> contextHolder = new ThreadLocal<>();

    public static void setUser(UserResponse userResponse) {
        contextHolder.set(userResponse);
    }

    public static UserResponse getUser() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}
