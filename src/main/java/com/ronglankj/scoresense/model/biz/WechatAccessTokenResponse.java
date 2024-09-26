package com.ronglankj.scoresense.model.biz;

import lombok.Builder;

@Builder
public record WechatAccessTokenResponse(
        String accessToken,
        Integer expiresIn
) {
}
