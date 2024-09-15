package com.ronglankj.scoresense.handler;

import com.ronglankj.scoresense.exception.BaseBizException;
import com.ronglankj.scoresense.exception.InvalidAuthenticationException;
import com.ronglankj.scoresense.model.response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidAuthenticationException(InvalidAuthenticationException e) {
        return e.composeResponse();
    }

    @ExceptionHandler(BaseBizException.class)
    public ResponseEntity<? extends ExceptionResponse> handleBaseBizException(BaseBizException e) {
        return e.composeResponse();
    }

}
