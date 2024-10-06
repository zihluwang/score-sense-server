package com.ahgtgk.scoresense.model.request;

public record OptionRequest(
        String id,
        String optionText,
        Boolean correct
) {
}
