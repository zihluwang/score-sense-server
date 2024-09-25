package com.ronglankj.scoresense.view;

import com.ronglankj.scoresense.enumeration.SwipeStatus;
import lombok.Builder;

@Builder
public record SwipeView(
        String id,
        String name,
        Integer status,
        String imageId
) {
}
