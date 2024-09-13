package com.ronglankj.scoresense.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import lombok.*;

/**
 * 考试，存储考试的基本信息。
 *
 * @author zihluwang
 */
@Table("exam")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Exam {

    /**
     * 考试 ID。
     */
    @Id(keyType = KeyType.None)
    private Long id;

    /**
     * 考试名称。
     */
    private String name;

    /**
     * 考试描述。
     */
    private String description;

    public static final ExamTableDef EXAM = new ExamTableDef();

    public static class ExamTableDef extends TableDef {

        public final QueryColumn ID = new QueryColumn(this, "id");

        public final QueryColumn NAME = new QueryColumn(this, "name");

        public final QueryColumn DESCRIPTION = new QueryColumn(this, "description");

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        public final QueryColumn[] DEFAULT_COLUMNS = {ID, NAME, DESCRIPTION};

        private ExamTableDef() {
            super("", "exam");
        }

        private ExamTableDef(String schema, String name, String alias) {
            super(schema, name, alias);
        }

        public ExamTableDef as(String alias) {
            var key = getNameWithSchema() + "." + alias;
            return getCache(key, (k) -> new ExamTableDef("", "exam", alias));
        }
    }

}
