package com.ahgtgk.scoresense.model.request;

import com.ahgtgk.scoresense.enumeration.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateSwipeRequest(
        @NotBlank(message = "轮播图名称不能为空")
        String name,
        Status status,
        @NotNull(message = "轮播图图片不能为空")
        Long imageId
) {
}
