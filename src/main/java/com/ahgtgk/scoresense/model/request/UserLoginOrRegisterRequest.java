package com.ahgtgk.scoresense.model.request;

/**
 * 用户登录或注册请求。
 *
 * @param code 微信小程序登录时的微信小程序提供的 {@code code}
 * @author zihluwang
 */
public record UserLoginOrRegisterRequest(
    String code
) {
}
