package com.ronglankj.scoresense;

import com.ronglankj.scoresense.cache.WechatCache;
import com.ronglankj.scoresense.service.WechatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"db", "wechat", "dev", "cache"})
class ScoreSenseApplicationTests {

    @Autowired
    private WechatService wechatService;
    @Autowired
    private WechatCache wechatCache;

    @Test
    void contextLoads() {
        var wechatToken = wechatService.getAccessToken();
        System.out.println(wechatToken);
    }

}
