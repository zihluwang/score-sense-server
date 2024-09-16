package com.ronglankj.scoresense.view;

import lombok.Builder;

/**
 * 用户视图
 *
 * @param id          用户 ID
 * @param openId      微信开放平台用户 ID
 * @param username    用户名，即微信昵称
 * @param phoneNumber 用户手机号码
 * @param avatarUrl   用户头像 URL
 * @param isBlocked   用户是否被封禁
 * @param token       用户身份令牌
 * @author zihluwang
 */
@Builder
public record UserView(
        Long id,
        String openId,
        String username,
        String phoneNumber,
        String avatarUrl,
        Boolean isBlocked,
        String token
) {

}
