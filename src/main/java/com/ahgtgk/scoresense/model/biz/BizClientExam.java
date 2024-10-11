package com.ahgtgk.scoresense.model.biz;

import com.ahgtgk.scoresense.enumeration.Status;
import com.ahgtgk.scoresense.view.ClientExamView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizClientExam {

    private Long id;

    /**
     * 考试名称。
     */
    private String name;

    /**
     * 考试类型。
     */
    private Integer type;

    /**
     * 考试描述。
     */
    private String description;

    /**
     * 举办考试省份。
     */
    private String province;

    /**
     * 举办考试城市。
     */
    private String prefecture;

    /**
     * 数据的展示基数。
     */
    private Integer attendeeCount;

    /**
     * 考试的状态。
     */
    private Status status;

    private LocalDateTime releasedAt;

    public ClientExamView toView() {
        return ClientExamView.builder()
                .id(String.valueOf(id))
                .name(name)
                .type(type)
                .description(description)
                .province(province)
                .prefecture(prefecture)
                .status(status.getCode())
                .attendeeCount(attendeeCount)
                .releasedAt(releasedAt)
                .build();
    }
}
