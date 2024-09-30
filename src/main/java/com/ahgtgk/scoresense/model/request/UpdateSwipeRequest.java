package com.ahgtgk.scoresense.model.request;

import com.ahgtgk.scoresense.enumeration.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateSwipeRequest(
        @NotNull(message = "轮播图 ID 不能为空")
        Long id,
        String name,
        Status status,
        Long imageId
) {
}
