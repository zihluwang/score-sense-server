package com.ahgtgk.scoresense.entity;

import com.ahgtgk.scoresense.view.AnswerView;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 用户答案，用于存储用户提交的答案，支持选择题和问答题。
 *
 * @author zihluwang
 */
@Table("answer")
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
    @Id(keyType = KeyType.None)
    private Long examId;

    /**
     * 试题 ID。
     */
    @Id(keyType = KeyType.None)
    private Long questionId;

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

    public static final AnswerTableDef ANSWER = new AnswerTableDef();

    public static class AnswerTableDef extends TableDef {

        public final QueryColumn EXAM_ID = new QueryColumn(this, "exam_id");

        public final QueryColumn QUESTION_ID = new QueryColumn(this, "question_id");

        public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

        public final QueryColumn ANSWER_TEXT = new QueryColumn(this, "answer_text");

        public final QueryColumn SUBMITTED_AT = new QueryColumn(this, "submitted_at");

        public final QueryColumn SCORE = new QueryColumn(this, "score");

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        public final QueryColumn[] DEFAULT_COLUMNS = {EXAM_ID, QUESTION_ID, USER_ID, ANSWER_TEXT, SUBMITTED_AT,
                SCORE};

        private AnswerTableDef() {
            super("", "answer");
        }

        private AnswerTableDef(String schema, String name, String alias) {
            super(schema, name, alias);
        }

        public AnswerTableDef as(String alias) {
            var key = getNameWithSchema() + "." + alias;
            return getCache(key, (k) -> new AnswerTableDef("", "admin", alias));
        }
    }

    public AnswerView toView() {
        return AnswerView.builder()
                .examId(String.valueOf(examId))
                .questionId(String.valueOf(questionId))
                .userId(String.valueOf(userId))
                .answerText(answerText)
                .score(score)
                .submittedAt(submittedAt)
                .build();
    }

}
