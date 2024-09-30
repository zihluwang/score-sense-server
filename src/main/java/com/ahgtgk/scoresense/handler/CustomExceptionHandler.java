package com.ahgtgk.scoresense.handler;

import com.ahgtgk.scoresense.exception.BizException;
import com.ahgtgk.scoresense.exception.InvalidAuthenticationException;
import com.ahgtgk.scoresense.holder.RequestContextHolder;
import com.ahgtgk.scoresense.model.response.ExceptionResponse;
import com.ahgtgk.scoresense.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidAuthenticationException(InvalidAuthenticationException e) {
        return e.composeResponse();
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<? extends ExceptionResponse> handleBaseBizException(BizException e) {
        return e.composeResponse();
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponse> handleBindException(BindException bindException) {
        var message = bindException.getAllErrors()
                .getFirst()
                .getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.builder()
                        .message(message)
                        .requestId(RequestContextHolder.getRequestContext().getRequestId())
                        .timestamp(DateTimeUtils.toInstant(LocalDateTime.now()))
                        .build());
    }

}
