package com.ahgtgk.scoresense.exception;

import org.springframework.http.HttpStatus;

public class PropertyExistedException extends BizException {

    public PropertyExistedException(String propertyName, String propertyValue) {
        super(HttpStatus.CONFLICT, propertyName + "（" + propertyValue + "）" + "已存在。");
    }
}
