package com.ahgtgk.scoresense.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 创建考试的请求。
 *
 * @param name       考试的名称
 * @param province   举办考试的省份代码
 * @param prefecture 举办考试的城市代码
 */
public record CreateExamRequest(
        @NotBlank(message = "考试名称不能为空")
        String name,
        @NotNull(message = "考试类型不能为空")
        Integer type,
        String description,
        @NotBlank(message = "考试举办地不能为空")
        String province,
        @NotBlank(message = "考试举办地不能为空")
        String prefecture
) {
}
