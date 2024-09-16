package com.ronglankj.scoresense.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.onixbyte.guid.GuidCreator;
import com.onixbyte.simplejwt.TokenResolver;
import com.ronglankj.scoresense.entity.User;
import com.ronglankj.scoresense.model.request.UserLoginOrRegisterRequest;
import com.ronglankj.scoresense.property.WeChatProperty;
import com.ronglankj.scoresense.service.UserService;
import com.ronglankj.scoresense.view.UserView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final TokenResolver<DecodedJWT> tokenResolver;
    private final WeChatProperty weChatProperty;
    private final UserService userService;
    private final GuidCreator<Long> userIdCreator;

    public UserController(TokenResolver<DecodedJWT> tokenResolver,
                          WeChatProperty weChatProperty,
                          UserService userService,
                          @Qualifier("userIdCreator") GuidCreator<Long> userIdCreator) {
        this.tokenResolver = tokenResolver;
        this.weChatProperty = weChatProperty;
        this.userService = userService;
        this.userIdCreator = userIdCreator;
    }

    @PostMapping("/login")
    public UserView login(@RequestBody UserLoginOrRegisterRequest request) {
        // 获取微信用户信息
        var weChatUserInfo = userService.getWeChatUserInfo(request.code());

        // 根据微信用户 OpenID 查询用户数据库信息
        var user = userService.getUserByOpenId(weChatUserInfo.openId());

        if (Objects.isNull(user)) { // 用户信息为空，注册账户
            var userId = userIdCreator.nextId();
            user = User.builder()
                    .id(userId)
                    .username("用户" + userId)
                    .isBlocked(false)
                    .openId(weChatUserInfo.openId())
                    .phoneNumber("")
                    .avatarUrl("") // todo 设置一个默认的头像地址
                    .build();
            userService.createUser(user);
        }

        // 创建用户令牌
        var token = tokenResolver.createToken(Duration.ofDays(30), user.getUsername(), "ScoreSense-Miniapp-User", user.toPayload());
        // 返回用户信息
        return UserView.builder()
                .id(user.getId())
                .openId(user.getOpenId())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .isBlocked(user.getIsBlocked())
                .token(token)
                .build();
    }

}
