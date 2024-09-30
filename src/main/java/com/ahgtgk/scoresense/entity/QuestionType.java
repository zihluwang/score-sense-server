package com.ahgtgk.scoresense.entity;

import com.mybatisflex.annotation.EnumValue;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import lombok.*;

/**
 * 试题类型
 *
 * @author zihluwang
 */
@Table("question_type")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionType {

    @Id(keyType = KeyType.None)
    private Integer id;

    private String label;

    public static class QuestionTypeTableDef extends TableDef {
        public final QueryColumn ID = new QueryColumn(this, "id");

        public final QueryColumn LABEL = new QueryColumn(this, "label");

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        public final QueryColumn[] DEFAULT_COLUMNS = {ID, LABEL};

        private QuestionTypeTableDef() {
            super("", "question_type");
        }

        private QuestionTypeTableDef(String schema, String tableName, String alias) {
            super(schema, tableName, alias);
        }

        public QuestionTypeTableDef as(String alias) {
            var key = getNameWithSchema() + "." + alias;
            return getCache(key, (k) -> new QuestionTypeTableDef("", "question_type", alias));
        }
    }

}
