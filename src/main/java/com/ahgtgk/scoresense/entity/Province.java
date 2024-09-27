package com.ahgtgk.scoresense.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import lombok.*;

@Table("province")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Province {

    @Id(keyType = KeyType.None)
    private String code;

    private String name;

    public static final ProvinceTableDef PROVINCE = new ProvinceTableDef();

    public static class ProvinceTableDef extends TableDef {
        public final QueryColumn CODE = new QueryColumn(this, "code");

        public final QueryColumn NAME = new QueryColumn(this, "name");

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        public final QueryColumn[] DEFAULT_COLUMNS = {CODE, NAME};

        private ProvinceTableDef() {
            super("", "province");
        }

        private ProvinceTableDef(String schema, String name, String alias) {
            super(schema, name, alias);
        }

        public ProvinceTableDef as(String alias) {
            var key = getNameWithSchema() + "." + alias;
            return getCache(key, (k) -> new ProvinceTableDef("", "province", alias));
        }
    }
}
