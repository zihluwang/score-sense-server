package com.ronglankj.scoresense.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import com.ronglankj.scoresense.enumeration.QuestionType;
import lombok.*;

/**
 * 题目，用于存储每个题目的基本信息。
 *
 * @author zihluwang
 */
@Table("question")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Question {

    /**
     * 考试 ID。
     */
    @Id(keyType = KeyType.None)
    private Long examId;

    /**
     * 题目 ID。
     */
    @Id(keyType = KeyType.None)
    private Long id;

    /**
     * 考试类型。
     */
    private QuestionType type;

    /**
     * 题干。
     */
    private String questionText;

    /**
     * 题目图像 ID。
     */
    private Long imageId;

    /**
     * 题目满分，将实际成绩 * 100 存储，如 {@code 100} 分存储为 {@code 10,000}。
     */
    private Integer maxScore;

    public static final QuestionTableDef QUESTION = new QuestionTableDef();

    public static class QuestionTableDef extends TableDef {

        public final QueryColumn EXAM_ID = new QueryColumn(this, "exam_id");

        public final QueryColumn ID = new QueryColumn(this, "id");

        public final QueryColumn TYPE = new QueryColumn(this, "type");

        public final QueryColumn QUESTION_TEXT = new QueryColumn(this, "question_text");

        public final QueryColumn IMAGE_ID = new QueryColumn(this, "image_id");

        public final QueryColumn MAX_SCORE = new QueryColumn(this, "max_score");

        public final QueryColumn[] DEFAULT_COLUMNS = {EXAM_ID, ID, TYPE, QUESTION_TEXT, IMAGE_ID, MAX_SCORE};

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        private QuestionTableDef() {
            super("", "question");
        }

        private QuestionTableDef(String schema, String name, String alias) {
            super(schema, name, alias);
        }

        public QuestionTableDef as(String alias) {
            var key = getNameWithSchema() + "." + alias;
            return getCache(key, (k) -> new QuestionTableDef("", "question", alias));
        }
    }

}
