package com.ahgtgk.scoresense.exception;

import org.springframework.http.HttpStatus;

public class UnauthenticatedException extends BizException {

    public UnauthenticatedException() {
        super(HttpStatus.UNAUTHORIZED, "无法获取用户身份。");
    }

}
