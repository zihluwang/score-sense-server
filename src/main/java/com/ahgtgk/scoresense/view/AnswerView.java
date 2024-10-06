package com.ahgtgk.scoresense.view;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AnswerView(
        String examId,
        String questionId,
        String userId,
        String answerText,
        LocalDateTime submittedAt,
        Integer score
) {
}
