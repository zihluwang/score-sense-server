package com.ahgtgk.scoresense.entity;

import com.ahgtgk.scoresense.view.ExamTypeView;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import lombok.*;

@Table("exam_type")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ExamType {

    @Id(keyType = KeyType.None)
    private Integer id;

    private String name;

    public static final ExamTypeTableDef EXAM_TYPE = new ExamTypeTableDef();

    public ExamTypeView toView() {
        return ExamTypeView.builder()
                .id(String.valueOf(id))
                .name(name)
                .build();
    }

    public static class ExamTypeTableDef extends TableDef {

        public final QueryColumn ID = new QueryColumn(this, "id");

        public final QueryColumn NAME = new QueryColumn(this, "name");

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        public final QueryColumn[] DEFAULT_COLUMNS = {ID, NAME};

        private ExamTypeTableDef() {
            super("", "exam_type");
        }

        private ExamTypeTableDef(String schema, String name, String alias) {
            super(schema, name, alias);
        }

        public ExamTypeTableDef as(String alias) {
            var key = getNameWithSchema() + "." + alias;
            return getCache(key, (k) -> new ExamTypeTableDef("", "exam_type", alias));
        }
    }

}
