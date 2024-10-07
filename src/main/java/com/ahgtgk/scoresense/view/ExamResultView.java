package com.ahgtgk.scoresense.view;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExamResultView(
        String examId,
        String userId,
        String vacancyId,
        Integer score,
        LocalDateTime completedAt
) {
}
