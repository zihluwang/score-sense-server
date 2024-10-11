package com.ahgtgk.scoresense.model.request;

import com.ahgtgk.scoresense.enumeration.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 创建考试的请求。
 *
 * @param name       考试的名称
 * @param province   举办考试的省份代码
 * @param prefecture 举办考试的城市代码
 */
@Builder
public record CreateExamRequest(
        @NotBlank(message = "考试名称不能为空")
        String name,
        @NotNull(message = "考试类型不能为空")
        Integer type,
        String description,
        @NotBlank(message = "考试举办地不能为空")
        String province,
        @NotBlank(message = "考试举办地不能为空")
        String prefecture,
        Status status,
        Integer baseNum,
        LocalDateTime releasedAt
) {
}
