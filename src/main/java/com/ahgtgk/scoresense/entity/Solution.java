package com.ahgtgk.scoresense.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import lombok.*;

@Table("solution")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Solution {

    @Id(keyType = KeyType.None)
    private Long examId;

    @Id(keyType = KeyType.None)
    private Long questionId;

    private String solutionText;

    public static final SolutionTableDef SOLUTION = new SolutionTableDef();

    public static class SolutionTableDef extends TableDef {

        public final QueryColumn EXAM_ID = new QueryColumn(this, "exam_id");

        public final QueryColumn QUESTION_ID = new QueryColumn(this, "question_id");

        public final QueryColumn SOLUTION_TEXT = new QueryColumn(this, "solution_text");

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        public final QueryColumn[] DEFAULT_COLUMNS = {EXAM_ID, QUESTION_ID, SOLUTION_TEXT};

        private SolutionTableDef() {
            super("", "solution");
        }

        private SolutionTableDef(String schema, String name, String alias) {
            super(schema, name, alias);
        }

        public SolutionTableDef as(String alias) {
            var key = getNameWithSchema() + "." + alias;
            return getCache(key, (k) -> new SolutionTableDef("", "solution", alias));
        }
    }

}
