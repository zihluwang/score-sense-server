package com.ahgtgk.scoresense.model.request;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record UploadAttachmentRequest(
        MultipartFile file,
        String name
) {
}
