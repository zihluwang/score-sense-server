package com.ahgtgk.scoresense.model.request;

import com.ahgtgk.scoresense.enumeration.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateExamRequest(
        @NotNull(message = "考试 ID 不能为空")
        Long id,
        String name,
        Integer type,
        String description,
        String province,
        String prefecture,
        Status status
) {
}
