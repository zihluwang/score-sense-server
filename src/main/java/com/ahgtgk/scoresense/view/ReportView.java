package com.ahgtgk.scoresense.view;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReportView(
    String examId,
    String examName,
    String userId,
    String vacancyId,
    Integer score,
    Integer totalScore,
    LocalDateTime completedAt,
    Integer rank,
    Integer attendeeCount,
    Integer correctAnswerCount,
    Integer totalQuestionCount,
    Integer averageScore,
    Double percentileRank
) {
}
