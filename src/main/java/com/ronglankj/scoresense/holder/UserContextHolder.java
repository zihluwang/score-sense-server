package com.ronglankj.scoresense.holder;

import com.ronglankj.scoresense.context.UserContext;

public class UserContextHolder {

    private static ThreadLocal<UserContext> userContextHolder =
            new InheritableThreadLocal<>();

    public static UserContext getUserContext() {
        return userContextHolder.get();
    }

    public static void setUserContext(UserContext userContext) {
        userContextHolder.set(userContext);
    }

    public static void removeUserContext() {
        userContextHolder.remove();
    }

}
