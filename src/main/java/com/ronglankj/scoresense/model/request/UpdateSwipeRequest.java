package com.ronglankj.scoresense.model.request;

import lombok.Builder;

@Builder
public record UpdateSwipeRequest(
        Long id,
        String name,
        Integer sequence,
        Long imageId
) {
}
