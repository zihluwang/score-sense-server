package com.ahgtgk.scoresense.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import lombok.*;

@Table("prefecture")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Prefecture {

    @Id(keyType = KeyType.None)
    private String code;

    private String name;

    private String provinceCode;

    public static final PrefectureTableDef PREFECTURE = new PrefectureTableDef();

    public static class PrefectureTableDef extends TableDef {
        public final QueryColumn CODE = new QueryColumn(this, "code");

        public final QueryColumn NAME = new QueryColumn(this, "name");

        public final QueryColumn PROVINCE_CODE = new QueryColumn(this, "province_code");

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        public final QueryColumn[] DEFAULT_COLUMNS = {CODE, NAME, PROVINCE_CODE};

        private PrefectureTableDef() {
            super("", "province");
        }

        private PrefectureTableDef(String schema, String name, String alias) {
            super(schema, name, alias);
        }

        public PrefectureTableDef as(String alias) {
            var key = getNameWithSchema() + "." + alias;
            return getCache(key, (k) -> new PrefectureTableDef("", "prefecture", alias));
        }
    }

}
