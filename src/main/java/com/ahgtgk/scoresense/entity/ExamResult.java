package com.ahgtgk.scoresense.entity;

import com.ahgtgk.scoresense.view.ExamResultView;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 考试结果，用于存储用户参加每场考试的总体成绩。
 *
 * @author zihluwang
 */
@Table("exam_result")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ExamResult {

    /**
     * 此次考试的 ID。
     */
    @Id(keyType = KeyType.None)
    private Long examId;

    /**
     * 参加此次考试的用户 ID。
     */
    @Id(keyType = KeyType.None)
    private Long userId;

    /**
     * 岗位 ID。
     */
    private Long vacancyId;

    /**
     * 该用户取得的总成绩，将实际成绩 * 100 存储，如 {@code 100} 分存储为 {@code 10,000}。
     */
    private Integer score;

    /**
     * 用户完成考试的时间。
     */
    private LocalDateTime completedAt;

    public static final ExamResultTableDef EXAM_RESULT = new ExamResultTableDef();

    public static class ExamResultTableDef extends TableDef {

        public final QueryColumn EXAM_ID = new QueryColumn(this, "exam_id");

        public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

        public final QueryColumn VACANCY_ID = new QueryColumn(this, "vacancy_id");

        public final QueryColumn SCORE = new QueryColumn(this, "score");

        public final QueryColumn COMPLETED_AT = new QueryColumn(this, "completed_at");

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        public final QueryColumn[] DEFAULT_COLUMNS = {EXAM_ID, USER_ID, VACANCY_ID, SCORE, COMPLETED_AT};

        private ExamResultTableDef() {
            super("", "exam_result");
        }

        private ExamResultTableDef(String schema, String name, String alias) {
            super(schema, name, alias);
        }

        public ExamResultTableDef as(String alisa) {
            var key = getNameWithSchema() + "." + alisa;
            return getCache(key, (k) -> new ExamResultTableDef("", "exam_result", alias));
        }
    }

    public ExamResultView toView() {
        return ExamResultView.builder()
                .examId(String.valueOf(examId))
                .userId(String.valueOf(userId))
                .vacancyId(String.valueOf(vacancyId))
                .score(score)
                .completedAt(completedAt)
                .build();
    }

}
