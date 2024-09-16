package com.ronglankj.scoresense.model.biz;

import lombok.Builder;

/**
 * 微信用户信息
 *
 * @param sessionKey 用户对话密钥
 * @param openId     微信用户唯一 ID
 * @author zihluwang
 */
@Builder
public record WeChatUserInfo(
        String sessionKey,
        String openId
) {
}
