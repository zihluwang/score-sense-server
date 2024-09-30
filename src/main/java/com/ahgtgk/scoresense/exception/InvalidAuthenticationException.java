package com.ahgtgk.scoresense.exception;

import com.ahgtgk.scoresense.holder.RequestContextHolder;
import com.ahgtgk.scoresense.model.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class InvalidAuthenticationException extends BizException {
    public InvalidAuthenticationException() {
        super(HttpStatus.UNAUTHORIZED, "用户名或密码错误。");
    }

    @Override
    public ResponseEntity<ExceptionResponse> composeResponse() {
        return ResponseEntity.status(getStatus())
                .body(ExceptionResponse.builder()
                        .message(getMessage())
                        .requestId(RequestContextHolder.getRequestContext().getRequestId())
                        .timestamp(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
                        .build());
    }
}
