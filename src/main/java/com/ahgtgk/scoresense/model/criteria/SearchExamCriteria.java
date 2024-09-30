package com.ahgtgk.scoresense.model.criteria;

import lombok.Builder;

@Builder
public record SearchExamCriteria(
        String divisionCode,
        String name
) {
}
