package com.ronglankj.scoresense.controller;

import com.ronglankj.scoresense.model.request.ShareQrcodeRequest;
import com.ronglankj.scoresense.service.WechatService;
import com.ronglankj.scoresense.view.AttachmentView;
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
