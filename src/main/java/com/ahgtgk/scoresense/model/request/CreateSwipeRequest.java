package com.ahgtgk.scoresense.model.request;

import com.ahgtgk.scoresense.enumeration.Status;
import lombok.Builder;

@Builder
public record CreateSwipeRequest(
        String name,
        Status status,
        Long imageId
) {
}
