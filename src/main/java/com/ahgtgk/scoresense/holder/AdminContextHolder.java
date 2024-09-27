package com.ahgtgk.scoresense.holder;

import com.ahgtgk.scoresense.context.AdminContext;

public class AdminContextHolder {

    private static final ThreadLocal<AdminContext> adminContextHolder = new InheritableThreadLocal<>();

    public static AdminContext getAdminContext() {
        return adminContextHolder.get();
    }

    public static void setAdminContext(AdminContext adminContext) {
        adminContextHolder.set(adminContext);
    }

    public static void removeAdminContext() {
        adminContextHolder.remove();
    }

}
