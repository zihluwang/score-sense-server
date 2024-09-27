package com.ahgtgk.scoresense.entity;

import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import lombok.*;

/**
 * 考试岗位。
 *
 * @author zihluwang
 */
@Table("exam_vacancy")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ExamVacancy {

    /**
     * 考试 ID。
     */
    private Long examId;

    /**
     * 岗位 ID。
     */
    private Long vacancyId;

    public static final ExamVacancyTableDef EXAM_VACANCY = new ExamVacancyTableDef();

    public static class ExamVacancyTableDef extends TableDef {

        public final QueryColumn EXAM_ID = new QueryColumn(this, "exam_id");

        public final QueryColumn VACANCY_ID = new QueryColumn(this, "vacancy_id");

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        public final QueryColumn[] DEFAULT_COLUMNS = {EXAM_ID, VACANCY_ID};

        private ExamVacancyTableDef() {
            super("", "exam_vacancy");
        }

        private ExamVacancyTableDef(String schema, String name, String alisa) {
            super(schema, name, alisa);
        }

        public ExamVacancyTableDef as(String alias) {
            var key = getNameWithSchema() + "." + alias;
            return getCache(key, (k) -> new ExamVacancyTableDef("", "exam_vacancy", alias));
        }
    }

}
