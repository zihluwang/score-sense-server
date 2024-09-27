package com.ahgtgk.scoresense.model.request;

import lombok.Builder;

@Builder
public record ExamQueryRequest(
        String divisionCode,
        String name
) {
}
