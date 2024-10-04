package com.ahgtgk.scoresense.view;

import com.ahgtgk.scoresense.enumeration.AnswerType;
import lombok.Builder;

import java.util.List;

@Builder
public record QuestionView(
        Long id,
        Integer type,
        AnswerType answerType,
        String questionText,
        Long imageId,
        Integer maxScore,
        List<OptionView> options
) {
}
