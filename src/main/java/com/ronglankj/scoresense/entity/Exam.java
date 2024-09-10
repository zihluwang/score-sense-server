package com.ronglankj.scoresense.entity;

import lombok.*;

/**
 * 考试，存储考试的基本信息。
 *
 * @author zihluwang
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Exam {

    /**
     * 考试 ID。
     */
    private Long id;

    /**
     * 考试名称。
     */
    private String name;

    /**
     * 考试描述。
     */
    private String description;

}
