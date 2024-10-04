package com.ahgtgk.scoresense.view;

import lombok.Builder;

@Builder
public record OptionView(
        String id,
        String optionText,
        Boolean correct
) {
}
