package com.ahgtgk.scoresense.exception;

import com.ahgtgk.scoresense.holder.RequestContextHolder;
import com.ahgtgk.scoresense.model.response.ExceptionResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 基本业务异常。
 *
 * @author zihluwang
 */
@Getter
public class BaseBizException extends RuntimeException {

    private final HttpStatus status;

    public BaseBizException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public ResponseEntity<? extends ExceptionResponse> composeResponse() {
        return ResponseEntity.status(status)
                .body(ExceptionResponse.builder()
                        .timestamp(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
                        .requestId(RequestContextHolder.getRequestContext().getRequestId())
                        .message(getMessage())
                        .build());
    }

}
