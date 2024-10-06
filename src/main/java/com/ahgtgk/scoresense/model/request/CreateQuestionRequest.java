package com.ahgtgk.scoresense.model.request;

import com.ahgtgk.scoresense.enumeration.AnswerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateQuestionRequest(
        @NotNull(message = "考试 ID 不能为空")
        Long examId,
        @NotNull(message = "题目 ID 不能为空")
        Long id,
        @NotNull(message = "题目类型不能为空")
        Integer type,
        @NotNull(message = "答题类型不能为空")
        AnswerType answerType,
        @NotBlank(message = "题目文本不能为空")
        String questionText,
        Long imageId,
        @NotNull(message = "最大分数不能为空")
        Integer maxScore,
        List<OptionRequest> options,
        String solution
) {
}
