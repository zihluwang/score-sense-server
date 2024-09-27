package com.ahgtgk.scoresense.controller;

import com.ahgtgk.scoresense.model.request.ShareQrcodeRequest;
import com.ahgtgk.scoresense.service.WechatService;
import com.ahgtgk.scoresense.view.AttachmentView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {

    private final WechatService wechatService;

    public CommonController(WechatService wechatService) {
        this.wechatService = wechatService;
    }

    @GetMapping("/share/qrcode")
    public AttachmentView getShareQrcode(@ModelAttribute ShareQrcodeRequest request) {
        return wechatService.fetchShareQrcode(request);
    }

}
