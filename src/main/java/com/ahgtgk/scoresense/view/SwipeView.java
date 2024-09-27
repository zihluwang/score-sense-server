package com.ahgtgk.scoresense.view;

import lombok.Builder;

@Builder
public record SwipeView(
        String id,
        String name,
        Integer status,
        String imageId
) {
}
