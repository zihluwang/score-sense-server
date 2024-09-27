package com.ahgtgk.scoresense.view;

import lombok.Builder;

@Builder
public record AttachmentView(
        String id,
        String name,
        String path,
        String contentType
) {
}
