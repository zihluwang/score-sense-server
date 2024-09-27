package com.ahgtgk.scoresense.model.request;

import lombok.Builder;

import java.util.List;

@Builder
public record UpdateVacancyRequest(
        Long id,
        String name,
        String province,
        String prefecture,
        List<Long> examIds
) {
}
