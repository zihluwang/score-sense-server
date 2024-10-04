package com.ahgtgk.scoresense.view;

import lombok.Builder;

import java.util.List;

@Builder
public record FullExamView(
        String id,
        String name,
        Integer type,
        String description,
        String province,
        String prefecture,
        Integer status,
        List<QuestionView> questions
) {
}
