package com.ahgtgk.scoresense.model.request;

import com.ahgtgk.scoresense.enumeration.AnswerType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateQuestionRequest(
        @NotNull(message = "考试 ID 不能为空")
        Long examId,
        @NotNull(message = "题目 ID 不能为空")
        Long id,
        Integer type,
        AnswerType answerType,
        String questionText,
        Long imageId,
        Integer maxScore,
        List<OptionRequest> options,
        String solution
) {
}
