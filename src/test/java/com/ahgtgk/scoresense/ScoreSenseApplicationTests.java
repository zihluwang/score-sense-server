package com.ahgtgk.scoresense;

import com.ahgtgk.scoresense.cache.WechatCache;
import com.ahgtgk.scoresense.entity.User;
import com.ahgtgk.scoresense.enumeration.Status;
import com.ahgtgk.scoresense.repository.UserRepository;
import com.ahgtgk.scoresense.service.WechatService;
import com.onixbyte.guid.GuidCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"db", "wechat", "dev", "cache"})
class ScoreSenseApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        System.out.println(passwordEncoder.encode("123456"));
    }

}
