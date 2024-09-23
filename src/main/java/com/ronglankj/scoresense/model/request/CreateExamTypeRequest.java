package com.ronglankj.scoresense.model.request;

import lombok.Builder;

@Builder
public record CreateExamTypeRequest(
        String name
) {
}
