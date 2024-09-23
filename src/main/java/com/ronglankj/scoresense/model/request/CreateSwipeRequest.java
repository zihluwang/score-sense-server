package com.ronglankj.scoresense.model.request;

import com.ronglankj.scoresense.enumeration.SwipeStatus;
import lombok.Builder;

@Builder
public record CreateSwipeRequest(
        String name,
        SwipeStatus status,
        Long imageId
) {
}
