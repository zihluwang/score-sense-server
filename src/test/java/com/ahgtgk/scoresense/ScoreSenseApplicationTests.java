package com.ahgtgk.scoresense;

import com.ahgtgk.scoresense.cache.WechatCache;
import com.ahgtgk.scoresense.service.WechatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"db", "wechat", "dev", "cache"})
class ScoreSenseApplicationTests {

    @Test
    void contextLoads() {
    }

}
