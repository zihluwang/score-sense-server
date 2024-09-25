package com.ronglankj.scoresense.model.request;

public record ShareQrcodeRequest(
        String scene,
        String page,
        Boolean checkPath,
        String envVersion,
        Integer width
) {
}
