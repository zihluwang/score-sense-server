package com.ronglankj.scoresense.model.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ActionResponse(
        String message,
        Instant timestamp
) {
}
