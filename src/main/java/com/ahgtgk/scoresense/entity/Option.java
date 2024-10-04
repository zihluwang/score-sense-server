package com.ahgtgk.scoresense.entity;

import com.ahgtgk.scoresense.model.biz.BizOption;
import com.ahgtgk.scoresense.model.biz.BizQuestion;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import lombok.*;

/**
 * 选项，用于存储选择题的选项信息，单选题和多选题都会用到。
 */
@Table("option")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Option {

    /**
     * 考试 ID
     */
    @Id(keyType = KeyType.None)
    private Long examId;

    /**
     * 题目 ID。
     */
    @Id(keyType = KeyType.None)
    private Long questionId;

    /**
     * 选项 ID。
     */
    @Id(keyType = KeyType.None)
    private String id;

    /**
     * 选项内容。
     */
    private String optionText;

    /**
     * 是否为正确选项。
     */
    private Boolean correct;

    public static final OptionTableDef OPTION = new OptionTableDef();

    public static class OptionTableDef extends TableDef {

        public final QueryColumn EXAM_ID = new QueryColumn(this, "exam_id");

        public final QueryColumn QUESTION_ID = new QueryColumn(this, "question_id");

        public final QueryColumn ID = new QueryColumn(this, "id");

        public final QueryColumn OPTION_TEXT = new QueryColumn(this, "option_text");

        public final QueryColumn CORRECT = new QueryColumn(this, "correct");

        public final QueryColumn[] DEFAULT_COLUMNS = {EXAM_ID, QUESTION_ID, ID, OPTION_TEXT, CORRECT};

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        private OptionTableDef() {
            super("", "option");
        }

        private OptionTableDef(String schema, String name, String alias) {
            super(schema, name, alias);
        }

        public OptionTableDef as(String alias) {
            var key = getNameWithSchema() + "." + alias;
            return getCache(key, (k) -> new OptionTableDef("", "option", alias));
        }
    }

    public BizOption toBiz() {
        return BizOption.builder()
                .id(id)
                .optionText(optionText)
                .correct(correct)
                .build();
    }

}
