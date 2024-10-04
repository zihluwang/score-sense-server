package com.ahgtgk.scoresense.model.request;

import com.ahgtgk.scoresense.enumeration.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record ImportExamRequest(
        @NotNull(message = "考试题目附件不能为空")
        MultipartFile attachment,
        Long id,
        String name,
        Integer type,
        String description,
        String province,
        String prefecture,
        Status status
) {
}
