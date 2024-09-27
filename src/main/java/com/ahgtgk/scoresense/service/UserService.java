package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.entity.User;
import com.ahgtgk.scoresense.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
