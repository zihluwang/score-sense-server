package com.ronglankj.scoresense.view;

import lombok.Builder;

@Builder
public record ExamView(
        String id,
        String name,
        Integer type,
        String description,
        String province,
        String prefecture
) {
}
