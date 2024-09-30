package com.ahgtgk.scoresense.controller;

import com.ahgtgk.scoresense.cache.UserCache;
import com.ahgtgk.scoresense.domain.UserDomain;
import com.ahgtgk.scoresense.entity.User;
import com.ahgtgk.scoresense.exception.UnauthenticatedException;
import com.ahgtgk.scoresense.repository.UserRepository;
import com.ahgtgk.scoresense.service.UserService;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.onixbyte.guid.GuidCreator;
import com.onixbyte.simplejwt.TokenResolver;
import com.ahgtgk.scoresense.model.request.UserLoginOrRegisterRequest;
import com.ahgtgk.scoresense.service.WechatService;
import com.ahgtgk.scoresense.view.UserView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final TokenResolver<DecodedJWT> tokenResolver;
    private final GuidCreator<Long> userIdCreator;
    private final WechatService wechatService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserCache userCache;

    public UserController(TokenResolver<DecodedJWT> tokenResolver,
                          @Qualifier("userIdCreator") GuidCreator<Long> userIdCreator,
                          WechatService wechatService,
                          PasswordEncoder passwordEncoder,
                          UserRepository userRepository,
                          UserService userService,
                          AuthenticationManager authenticationManager, UserCache userCache) {
        this.tokenResolver = tokenResolver;
        this.userIdCreator = userIdCreator;
        this.wechatService = wechatService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userCache = userCache;
    }

    @PostMapping("/login")
    public ResponseEntity<UserView> login(@RequestBody UserLoginOrRegisterRequest request) {
        var isAdmin = true;
        var user = User.builder().build();

        if (Objects.nonNull(request.code()) && !request.code().isBlank()) { // 执行微信用户登录逻辑
            var openId = wechatService.getWechatUserOpenId(request.code());
            user = Optional.ofNullable(userRepository.selectOneByCondition(User.USER.OPEN_ID.eq(openId)))
                    .orElseGet(() -> { // 用户不存在，执行注册
                        var userId = userIdCreator.nextId();
                        var _user = User.builder()
                                .id(userId)
                                .openId(openId)
                                .username("微信用户" + userId)
                                .password(passwordEncoder.encode(openId))
                                .nonLocked(true)
                                .avatarId(0L)
                                .phoneNumber("")
                                .build();
                        userService.createUser(_user);
                        return _user;
                    });
            isAdmin = false;
        }

        var authenticationToken = isAdmin ?
                UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password()) :
                UsernamePasswordAuthenticationToken.unauthenticated(user.getUsername(), user.getOpenId());
        var authentication = authenticationManager.authenticate(authenticationToken);

        if (authentication.getPrincipal() instanceof UserDomain userDomain) {
            var token = tokenResolver.createToken(Duration.ofDays(30),
                    "ScoreSense:" + userDomain.getUserType().name(),
                    String.valueOf(userDomain.getId()));
            userCache.putUser(userDomain.toPersistent());
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Authorization", token)
                    .body(userDomain.toView());
        } else {
            throw new UnauthenticatedException();
        }
    }

}
