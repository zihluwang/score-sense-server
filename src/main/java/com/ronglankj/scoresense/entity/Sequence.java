package com.ronglankj.scoresense.entity;

import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import lombok.*;

@Table("sequence")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Sequence {

    private String key;

    private Long next;

    public static final SequenceTableDef SEQUENCE = new SequenceTableDef();

    public static class SequenceTableDef extends TableDef {
        public final QueryColumn KEY = new QueryColumn(this, "key");

        public final QueryColumn NEXT = new QueryColumn(this, "next");

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        public final QueryColumn[] DEFAULT_COLUMNS = {KEY, NEXT};

        private SequenceTableDef() {
            super("", "sequence");
        }

        private SequenceTableDef(String schema, String tableName, String alias) {
            super(schema, tableName, alias);
        }

        public SequenceTableDef as(String alias) {
            var key = getNameWithSchema() + "." + alias;
            return getCache(key, (k) -> new SequenceTableDef("", "sequence", alias));
        }
    }

}
