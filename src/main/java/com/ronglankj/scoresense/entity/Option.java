package com.ronglankj.scoresense.entity;

import lombok.*;

/**
 * 选项，用于存储选择题的选项信息，单选题和多选题都会用到。
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Option {

    /**
     * 选项 ID。
     */
    private Long id;

    /**
     * 题目 ID。
     */
    private Long questionId;

    /**
     * 选项内容。
     */
    private String optionText;

    /**
     * 是否为正确选项。
     */
    private Boolean isCorrect;

}
