package com.ahgtgk.scoresense.model.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

    protected String message;

    protected Instant timestamp;

    protected String requestId;

}
