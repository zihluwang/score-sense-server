package com.ahgtgk.scoresense.model.criteria;

import lombok.Builder;

@Builder
public record SearchVacancyCriteria(
        String name,
        String divisionCode
) {
}
