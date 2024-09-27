package com.ahgtgk.scoresense.model.response;

import com.ahgtgk.scoresense.util.DateTimeUtils;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;

@Builder
public record ActionResponse(
        String message,
        Instant timestamp
) {

    public ActionResponse(String message) {
        this(message, DateTimeUtils.toInstant(LocalDateTime.now()));
    }
}
