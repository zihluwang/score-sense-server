package com.ronglankj.scoresense.entity;

import com.ronglankj.scoresense.enumeration.QuestionType;
import lombok.*;

/**
 * 题目，用于存储每个题目的基本信息。
 *
 * @author zihluwang
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Question {

    /**
     * 题目 ID。
     */
    private Long id;

    /**
     * 考试 ID。
     */
    private Long examId;

    /**
     * 考试类型。
     */
    private QuestionType type;

    /**
     * 题干。
     */
    private String questionText;

    /**
     * 题目满分，将实际成绩 * 100 存储，如 {@code 100} 分存储为 {@code 10,000}。
     */
    private Integer maxScore;

}
