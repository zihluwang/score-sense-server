package com.ahgtgk.scoresense.model.request;

/**
 * 用户登录或注册请求。
 *
 * @param code     微信小程序登录时的微信小程序提供的 {@code code}
 * @param username 用户名
 * @param password 密码
 * @author zihluwang
 */
public record UserLoginOrRegisterRequest(
        String code,
        String username,
        String password
) {
}
