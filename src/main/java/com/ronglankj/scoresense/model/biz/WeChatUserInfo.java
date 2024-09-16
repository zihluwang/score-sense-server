package com.ronglankj.scoresense.model.biz;

import lombok.Builder;

@Builder
public record WeChatUserInfo(
        String sessionKey,
        String openId
) {
}
