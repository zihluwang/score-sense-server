package com.ronglankj.scoresense.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onixbyte.guid.GuidCreator;
import com.ronglankj.scoresense.cache.WechatCache;
import com.ronglankj.scoresense.exception.BaseBizException;
import com.ronglankj.scoresense.extension.spring.ByteArrayMultipartFile;
import com.ronglankj.scoresense.model.biz.WechatAccessTokenResponse;
import com.ronglankj.scoresense.model.request.ShareQrcodeRequest;
import com.ronglankj.scoresense.model.request.UploadAttachmentRequest;
import com.ronglankj.scoresense.property.WechatProperty;
import com.ronglankj.scoresense.view.AttachmentView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class WechatService {


    private final WebClient wechatClient;
    private final WechatProperty wechatProperty;
    private final WechatCache wechatCache;
    private final ObjectMapper objectMapper;
    private final AttachmentService attachmentService;
    private final GuidCreator<Long> attachmentIdCreator;

    public WechatService(WebClient wechatClient,
                         WechatProperty wechatProperty,
                         WechatCache wechatCache,
                         ObjectMapper objectMapper,
                         AttachmentService attachmentService,
                         GuidCreator<Long> attachmentIdCreator) {
        this.wechatClient = wechatClient;
        this.wechatProperty = wechatProperty;
        this.wechatCache = wechatCache;
        this.objectMapper = objectMapper;
        this.attachmentService = attachmentService;
        this.attachmentIdCreator = attachmentIdCreator;
    }

    /**
     * 获取微信 AccessToken。
     *
     * @return 微信 AccessToken
     */
    protected String fetchAccessToken() {
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
            return plainAccessToken;
        }
        throw new BaseBizException(HttpStatus.SERVICE_UNAVAILABLE, "微信服务繁忙，请稍后再试");
    }

    public String getAccessToken() {
        var accessToken = wechatCache.getAccessToken();
        if (Objects.isNull(accessToken) || accessToken.isBlank()) {
            var response = fetchAccessToken();
            wechatCache.saveAccessToken(response);
            accessToken = response;
        }
        return accessToken;
    }

    public AttachmentView fetchShareQrcode(ShareQrcodeRequest request) {
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

        var image = wechatClient.post()
                .uri((uriBuilder) -> uriBuilder.path("/wxa/getwxacodeunlimit")
                        .queryParam("access_token", getAccessToken())
                        .build())
                .bodyValue(requestBody)
                .exchangeToMono((response) -> {
                    var contentType = response.headers().contentType().orElse(MediaType.APPLICATION_OCTET_STREAM);
                    var attachmentId = attachmentIdCreator.nextId();
                    return response.bodyToMono(byte[].class)
                            .map((content) -> new ByteArrayMultipartFile("小程序码-%d".formatted(attachmentId),
                                    contentType.toString(), content));
                })
                .block();

        if (Objects.isNull(image)) {
            throw new BaseBizException(HttpStatus.BAD_GATEWAY, "微信服务暂不可用，请稍后重试");
        }

        var attachment = attachmentService.saveAttachment(UploadAttachmentRequest.builder()
                .file(image)
                .name(image.getName())
                .build());

        return attachment.toView();
    }

    /**
     * 请求微信 API 获取微信用户 ID。
     *
     * @param code 微信用户一次性登录代码
     * @return 用户信息
     */
    public String getWechatUserOpenId(String code) {
        try {
            // 发送获取用户身份信息的请求
            var result = wechatClient.get()
                    .uri((uriBuilder) -> uriBuilder
                            .path("/sns/jscode2session")
                            .queryParam("appid", wechatProperty.getAppId())
                            .queryParam("secret", wechatProperty.getAppSecret())
                            .queryParam("js_code", code)
                            .queryParam("grant_type", "authorization_code")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class) // 微信的响应中，Content-Type 被设置为 text/plain
                    .block();

            // 由于微信接口中命名不符合 Java 中的参数命名规范，因此将数据转储为 Map<String, String>
            var resultMap = objectMapper.readValue(result, new TypeReference<Map<String, String>>() {
            });

            // errorCode == -1: 系统繁忙，稍后再试
            // errorCode == 0: 请求成功
            // errorCode == 40029: code 无效
            // errorCode == 45011: 频率限制，每个用户1分钟限量100次
            // errorCode == 40226: 高风险等级用户，小程序登录拦截

            return resultMap.get("openid");
        } catch (JsonProcessingException e) {
            throw new BaseBizException(HttpStatus.INTERNAL_SERVER_ERROR, "无法解析用户信息");
        }
    }

}
