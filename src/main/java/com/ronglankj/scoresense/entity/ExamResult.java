package com.ronglankj.scoresense.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 考试结果，用于存储用户参加每场考试的总体成绩。
 *
 * @author zihluwang
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ExamResult {

    /**
     * 考试结果 ID。
     */
    private Long id;

    /**
     * 此次考试的 ID。
     */
    private Long examId;

    /**
     * 参加此次考试的用户 ID。
     */
    private Long userId;

    /**
     * 该用户取得的总成绩，将实际成绩 * 100 存储，如 {@code 100} 分存储为 {@code 10,000}。
     */
    private Integer totalScore;

    /**
     * 用户完成考试的时间。
     */
    private LocalDateTime completedAt;

}
