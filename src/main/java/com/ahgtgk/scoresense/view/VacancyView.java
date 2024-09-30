package com.ahgtgk.scoresense.view;

import lombok.Builder;

@Builder
public record VacancyView(
        String id,
        String name,
        String province,
        String prefecture
) {
}
