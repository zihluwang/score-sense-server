package com.ahgtgk.scoresense.entity;

import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import lombok.*;

/**
 * 岗位信息
 */
@Table("vacancy")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Vacancy {

    private Long id;

    private String name;

    private String province;

    private String prefecture;

    public static final VacancyTableDef VACANCY = new VacancyTableDef();

    public static class VacancyTableDef extends TableDef {

        public final QueryColumn ID = new QueryColumn(this, "id");

        public final QueryColumn NAME = new QueryColumn(this, "name");

        public final QueryColumn PROVINCE = new QueryColumn(this, "province");

        public final QueryColumn PREFECTURE = new QueryColumn(this, "prefecture");

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        public final QueryColumn[] DEFAULT_COLUMNS = {ID, NAME, PROVINCE, PREFECTURE};

        private VacancyTableDef() {
            super("", "vacancy");
        }

        private VacancyTableDef(String schema, String name, String alisa) {
            super(schema, name, alisa);
        }

        @Override
        public VacancyTableDef as(String alias) {
            var key = getNameWithSchema() + "." + alias;
            return getCache(key, (k) -> new VacancyTableDef("", "vacancy", alias));
        }
    }

}
