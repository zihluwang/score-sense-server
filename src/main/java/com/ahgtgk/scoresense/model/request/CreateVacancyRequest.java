package com.ahgtgk.scoresense.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateVacancyRequest(
        Long id,
        @NotBlank(message = "岗位名称不能为空")
        String name,
        String province,
        String prefecture,
        List<Long> examIds
) {
}
