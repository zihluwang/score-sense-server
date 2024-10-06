package com.ahgtgk.scoresense.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record AnswerQuestionRequest(
        @NotNull(message = "考试 ID 不能为空")
        Long examId,
        @NotNull(message = "试题 ID 不能为空")
        Long questionId,
        @Size(min = 1, message = "用户回答不能为空")
        List<String> answerText
) {
}
