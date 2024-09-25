package com.ronglankj.scoresense.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ronglankj.scoresense.entity.User;
import com.ronglankj.scoresense.exception.BaseBizException;
import com.ronglankj.scoresense.model.biz.WechatUserInfo;
import com.ronglankj.scoresense.property.WechatProperty;
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

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
