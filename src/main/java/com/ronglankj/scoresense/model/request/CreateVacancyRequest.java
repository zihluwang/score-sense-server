package com.ronglankj.scoresense.model.request;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateVacancyRequest(
        String name,
        String province,
        String prefecture,
        List<Long> examIds
) {
}
