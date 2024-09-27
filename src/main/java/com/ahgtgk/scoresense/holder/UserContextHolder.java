package com.ahgtgk.scoresense.holder;

import com.ahgtgk.scoresense.context.UserContext;

public class UserContextHolder {

    private static final ThreadLocal<UserContext> userContextHolder =
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
