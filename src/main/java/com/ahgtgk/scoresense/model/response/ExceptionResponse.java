package com.ahgtgk.scoresense.model.response;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

    protected String message;

    protected Instant timestamp;

    protected String requestId;

}
