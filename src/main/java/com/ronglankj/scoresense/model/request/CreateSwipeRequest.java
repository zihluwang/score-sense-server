package com.ronglankj.scoresense.model.request;

import lombok.Builder;

@Builder
public record CreateSwipeRequest(
        String name,
        Integer sequence,
        Long imageId
) {
}
