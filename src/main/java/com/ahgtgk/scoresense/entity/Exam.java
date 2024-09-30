package com.ahgtgk.scoresense.entity;

import com.ahgtgk.scoresense.enumeration.Status;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import com.ahgtgk.scoresense.view.ExamView;
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
     * 考试类型。
     */
    private Integer type;

    /**
     * 考试描述。
     */
    private String description;

    /**
     * 举办考试省份。
     */
    private String province;

    /**
     * 举办考试城市。
     */
    private String prefecture;

    /**
     * 考试的状态
     */
    private Status status;

    public static final ExamTableDef EXAM = new ExamTableDef();

    public ExamView toView() {
        return ExamView.builder()
                .id(String.valueOf(id))
                .name(name)
                .type(type)
                .description(description)
                .province(province)
                .prefecture(prefecture)
                .status(status.getCode())
                .build();
    }

    public static class ExamTableDef extends TableDef {

        public final QueryColumn ID = new QueryColumn(this, "id");

        public final QueryColumn NAME = new QueryColumn(this, "name");

        public final QueryColumn TYPE = new QueryColumn(this, "type");

        public final QueryColumn DESCRIPTION = new QueryColumn(this, "description");

        public final QueryColumn PROVINCE = new QueryColumn(this, "province");

        public final QueryColumn PREFECTURE = new QueryColumn(this, "prefecture");

        public final QueryColumn STATUS = new QueryColumn(this, "status");

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        public final QueryColumn[] DEFAULT_COLUMNS = {ID, NAME, TYPE, DESCRIPTION, PROVINCE, PREFECTURE, STATUS};

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
