package com.ronglankj.scoresense.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ronglankj.scoresense.entity.User;
import com.ronglankj.scoresense.exception.BaseBizException;
import com.ronglankj.scoresense.model.biz.WeChatUserInfo;
import com.ronglankj.scoresense.property.WeChatProperty;
import com.ronglankj.scoresense.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
public class UserService {

    private final WeChatProperty weChatProperty;
    private final WebClient weChatClient;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    public UserService(
            WeChatProperty weChatProperty, WebClient weChatClient, ObjectMapper jacksonObjectMapper, UserRepository userRepository) {
        this.weChatProperty = weChatProperty;
        this.weChatClient = weChatClient;
        this.objectMapper = jacksonObjectMapper;
        this.userRepository = userRepository;
    }

    public WeChatUserInfo getWeChatUserInfo(String code) {
        try {
            var result = weChatClient.get()
                    .uri((uriBuilder) -> uriBuilder
                            .path("/sns/jscode2session")
                            .queryParam("appid", weChatProperty.getAppId())
                            .queryParam("secret", weChatProperty.getAppSecret())
                            .queryParam("js_code", code)
                            .queryParam("grant_type", "authorization_code")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            var resultMap = objectMapper.readValue(result, new TypeReference<Map<String, String>>() {
            });
            return WeChatUserInfo.builder()
                    .openId(resultMap.get("openid"))
                    .sessionKey(resultMap.get("session_key"))
                    .build();
        } catch (JsonProcessingException e) {
            throw new BaseBizException(HttpStatus.INTERNAL_SERVER_ERROR, "无法解析用户信息");
        }
    }

    public User getUserByOpenId(String openId) {
        return userRepository.selectOneByCondition(User.USER.OPEN_ID.eq(openId));
    }

    public int createUser(User user) {
        return userRepository.insert(user);
    }

}
