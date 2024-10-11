package com.ahgtgk.scoresense.entity;

import com.ahgtgk.scoresense.model.biz.BizVacancy;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import lombok.*;

import java.util.Collections;
import java.util.List;

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

    @Id(keyType = KeyType.None)
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

    public BizVacancy toBiz(List<Long> examIds) {
        return BizVacancy.builder()
                .id(id)
                .name(name)
                .province(province)
                .prefecture(prefecture)
                .examIds(examIds.stream().map(String::valueOf).toList())
                .build();
    }

    public BizVacancy toBiz() {
        return toBiz(Collections.emptyList());
    }

}
