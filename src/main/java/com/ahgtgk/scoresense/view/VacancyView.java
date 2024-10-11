package com.ahgtgk.scoresense.view;

import lombok.Builder;

import java.util.List;

@Builder
public record VacancyView(
        String id,
        String name,
        String province,
        String prefecture,
        List<String> examIds
) {
}
