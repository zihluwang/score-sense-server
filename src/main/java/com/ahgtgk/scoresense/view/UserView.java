package com.ahgtgk.scoresense.view;

import lombok.Builder;

/**
 * 用户视图
 *
 * @param id          用户 ID
 * @param username    用户名，即微信昵称
 * @param phoneNumber 用户手机号码
 * @param avatarId    用户头像 URL
 * @param nonLocked   用户是否被封禁
 * @author zihluwang
 */
@Builder
public record UserView(
        String id,
        String username,
        String phoneNumber,
        String avatarId,
        Boolean nonLocked
) {

}
