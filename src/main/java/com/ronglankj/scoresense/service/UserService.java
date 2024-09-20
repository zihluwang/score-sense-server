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

/**
 * 用户服务。
 *
 * @author zihluwang
 */
@Slf4j
@Service
public class UserService {

    private final WeChatProperty weChatProperty;
    private final WebClient weChatClient;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    public UserService(WeChatProperty weChatProperty,
                       WebClient weChatClient,
                       ObjectMapper jacksonObjectMapper,
                       UserRepository userRepository) {
        this.weChatProperty = weChatProperty;
        this.weChatClient = weChatClient;
        this.objectMapper = jacksonObjectMapper;
        this.userRepository = userRepository;
    }

    /**
     * 请求微信 API 获取微信用户 ID。
     *
     * @param code 微信用户一次性登录代码
     * @return 用户信息
     */
    public WeChatUserInfo getWeChatUserInfo(String code) {
        try {
            // 发送获取用户身份信息的请求
            var result = weChatClient.get()
                    .uri((uriBuilder) -> uriBuilder
                            .path("/sns/jscode2session")
                            .queryParam("appid", weChatProperty.getAppId())
                            .queryParam("secret", weChatProperty.getAppSecret())
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

            return WeChatUserInfo.builder()
                    .openId(resultMap.get("openid"))
                    .sessionKey(resultMap.get("session_key"))
                    .build();
        } catch (JsonProcessingException e) {
            throw new BaseBizException(HttpStatus.INTERNAL_SERVER_ERROR, "无法解析用户信息");
        }
    }

    /**
     * 根据用户的微信 OpenID 查找用户信息。
     *
     * @param openId 用户的微信 Open ID
     * @return 查找到的用户信息，如果该微信用户未注册账户则返回 {@code null}
     */
    public User getUserByOpenId(String openId) {
        return userRepository.selectOneByCondition(User.USER.OPEN_ID.eq(openId));
    }

    /**
     * 创建用户。
     *
     * @param user 用户信息
     * @return {@code 1} 代表用户创建成功，{@code 0} 代表用户创建失败
     */
    public int createUser(User user) {
        return userRepository.insert(user);
    }

}
