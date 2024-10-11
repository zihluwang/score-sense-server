package com.ahgtgk.scoresense.view;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ClientExamView(
        String id,
        String name,
        Integer type,
        String description,
        String province,
        String prefecture,
        Integer status,
        Integer attendeeCount,
        LocalDateTime releasedAt
) {
}
