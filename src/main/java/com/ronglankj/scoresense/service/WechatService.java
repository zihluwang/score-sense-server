package com.ronglankj.scoresense.service;

import com.ronglankj.scoresense.cache.WechatCache;
import com.ronglankj.scoresense.exception.BaseBizException;
import com.ronglankj.scoresense.model.biz.WechatAccessTokenResponse;
import com.ronglankj.scoresense.model.request.ShareQrcodeRequest;
import com.ronglankj.scoresense.property.WechatProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

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
            wechatCache.saveAccessToken(response.accessToken());
            accessToken = response.accessToken();
        }
        return accessToken;
    }

    public String fetchShareQrcode(ShareQrcodeRequest request) {
        var requestBody = new HashMap<String, Object>();

        // 微信要求 scene 必填
        requestBody.put("scene", Optional.ofNullable(request.scene()).orElse(""));
        Optional.ofNullable(request.page())
                .ifPresent((page) -> requestBody.put("page", page));
        Optional.ofNullable(request.checkPath())
                .ifPresent((checkPath) -> requestBody.put("check_path", checkPath));
        Optional.ofNullable(request.envVersion())
                .ifPresent((envVersion) -> requestBody.put("env_version", envVersion));
        Optional.ofNullable(request.width())
                .ifPresent((width) -> requestBody.put("width", width));

        return wechatClient.post()
                .uri((uriBuilder) -> uriBuilder.path("/wxa/getwxacodeunlimit")
                        .queryParam("access_token", loadAccessToken())
                        .build())
                .bodyValue(requestBody)
                .exchangeToMono((_response) -> {
                    var _contentType = _response.headers().contentType().orElse(MediaType.APPLICATION_OCTET_STREAM);
                    return _response.bodyToMono(byte[].class)
                            .map((bytes) -> {
                                var base64String = Base64.getEncoder().encodeToString(bytes);
                                return "data:%s;base64, %s".formatted(_contentType.toString(), base64String);
                            });
                })
                .block();
    }

}
