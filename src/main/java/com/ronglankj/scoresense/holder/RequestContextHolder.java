package com.ronglankj.scoresense.holder;

import com.ronglankj.scoresense.context.RequestContext;

public class RequestContextHolder {

    private static final ThreadLocal<RequestContext> requestContextHolder = new InheritableThreadLocal<>();

    public static void setRequestContext(RequestContext requestContext) {
        requestContextHolder.set(requestContext);
    }

    public static RequestContext getRequestContext() {
        return requestContextHolder.get();
    }

    public static void removeRequestContext() {
        requestContextHolder.remove();
    }

}
