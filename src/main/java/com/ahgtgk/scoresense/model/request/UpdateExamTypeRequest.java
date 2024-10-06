package com.ahgtgk.scoresense.model.request;

import jakarta.validation.constraints.NotNull;

public record UpdateExamTypeRequest(
        @NotNull(message = "考试类型 ID 不能为空") Integer id,
        String name
) {
}
