package com.ahgtgk.scoresense;

import com.ahgtgk.scoresense.cache.WechatCache;
import com.ahgtgk.scoresense.entity.User;
import com.ahgtgk.scoresense.repository.UserRepository;
import com.ahgtgk.scoresense.service.WechatService;
import com.onixbyte.guid.GuidCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"db", "wechat", "dev", "cache"})
class ScoreSenseApplicationTests {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Qualifier("userIdCreator")
    @Autowired
    private GuidCreator<Long> userIdCreator;

    @Test
    void contextLoads() {
        userRepository.insert(User.builder()
                .id(userIdCreator.nextId())
                .username("zhangsan")
                .password(passwordEncoder.encode("zhangsan"))
                .phoneNumber("")
                .avatarId(0L)
                .nonLocked(true)
                .build());
    }

}
