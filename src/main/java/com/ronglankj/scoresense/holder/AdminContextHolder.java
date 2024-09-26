package com.ronglankj.scoresense.holder;

import com.ronglankj.scoresense.context.AdminContext;

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
