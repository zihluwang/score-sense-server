package com.ronglankj.scoresense.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 用户答案，用于存储用户提交的答案，支持选择题和问答题。
 *
 * @author zihluwang
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Answer {

    /**
     * 考试 ID。
     */
    private Long examId;

    /**
     * 试题 ID。
     */
    private Long questionId;

    /**
     * 答案 ID。
     */
    private Long id;

    /**
     * 答题用户 ID。
     */
    private Long userId;

    /**
     * 用户的答案（多选题时的多个值，以英文逗号 {@code ,} 分隔）。
     */
    private String answerText;

    /**
     * 答案提交时间。
     */
    private LocalDateTime submittedAt;

    /**
     * 用户在该题获得的分数，将实际成绩 * 100 存储，如 {@code 100} 分存储为 {@code 10,000}。
     */
    private Integer score;

}
