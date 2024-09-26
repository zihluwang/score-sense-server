package com.ronglankj.scoresense.model.response;

import lombok.Builder;

@Builder
public record PrefectureResponse(
        String code,
        String name
) {
}
