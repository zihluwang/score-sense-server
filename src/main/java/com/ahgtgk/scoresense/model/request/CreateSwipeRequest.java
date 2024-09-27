package com.ahgtgk.scoresense.model.request;

import com.ahgtgk.scoresense.enumeration.SwipeStatus;
import lombok.Builder;

@Builder
public record CreateSwipeRequest(
        String name,
        SwipeStatus status,
        Long imageId
) {
}
