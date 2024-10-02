package com.ahgtgk.scoresense.exception;

import org.springframework.http.HttpStatus;

public class DataConflictException extends BizException{

    public DataConflictException() {
        super(HttpStatus.CONFLICT, "数据存在冲突");
    }

    public DataConflictException(String propertyName) {
        super(HttpStatus.CONFLICT, "数据【" + propertyName + "】存在冲突");
    }
}
