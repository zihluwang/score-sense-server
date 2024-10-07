package com.ahgtgk.scoresense.view;

import lombok.Builder;

@Builder
public record ScoreAnalysisView(
        String scoreRange,
        Integer attendeeCount
) {
}
