package com.ronglankj.scoresense.service;

import com.ronglankj.scoresense.cache.WechatCache;
import com.ronglankj.scoresense.exception.BaseBizException;
import com.ronglankj.scoresense.model.biz.WechatAccessTokenResponse;
import com.ronglankj.scoresense.property.WechatProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Objects;

@Slf4j
@Service
public class WechatService {


    private final WebClient wechatClient;
    private final WechatProperty wechatProperty;
    private final WechatCache wechatCache;

    public WechatService(WebClient wechatClient,
                         WechatProperty wechatProperty, WechatCache wechatCache) {
        this.wechatClient = wechatClient;
        this.wechatProperty = wechatProperty;
        this.wechatCache = wechatCache;
    }

    protected WechatAccessTokenResponse fetchAccessToken() {
        var response = wechatClient.get()
                .uri((uriBuilder) -> uriBuilder.path("/cgi-bin/token")
                        .queryParam("grant_type", "client_credential")
                        .queryParam("appid", wechatProperty.getAppId())
                        .queryParam("secret", wechatProperty.getAppSecret())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<HashMap<String, Object>>() {
                })
                .block();

        if (Objects.isNull(response)) {
            throw new BaseBizException(HttpStatus.INTERNAL_SERVER_ERROR, "与微信的网络连接中断，请稍后再试");
        }

        var accessTokenBuilder = WechatAccessTokenResponse.builder();

        var responseAccessToken = response.get("access_token");
        if (responseAccessToken instanceof String plainAccessToken) {
            accessTokenBuilder.accessToken(plainAccessToken);
        }

        var responseExpiresIn = response.get("expires_in");
        if (responseExpiresIn instanceof Integer plainExpiresIn) {
            accessTokenBuilder.expiresIn(plainExpiresIn);
        }

        return accessTokenBuilder.build();
    }

    public String loadAccessToken() {
        var accessToken = wechatCache.getAccessToken();
        if (Objects.isNull(accessToken) || accessToken.isBlank()) {
            var response = fetchAccessToken();
            wechatCache.saveAccessToken(response);
            accessToken = response.accessToken();
        }
        return accessToken;
    }

}
