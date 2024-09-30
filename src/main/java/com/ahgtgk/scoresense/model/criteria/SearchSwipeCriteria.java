package com.ahgtgk.scoresense.model.criteria;

import com.ahgtgk.scoresense.enumeration.Status;
import lombok.Builder;

@Builder
public record SearchSwipeCriteria(
        String name,
        Status status
) {
}
