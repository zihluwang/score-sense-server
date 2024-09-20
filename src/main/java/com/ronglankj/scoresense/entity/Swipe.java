package com.ronglankj.scoresense.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import lombok.*;

/**
 * 轮播图
 */
@Table("swipe")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Swipe {

    @Id
    private Integer sequence;

    private String imageUrl;

    public static final SwipeTableDef SWIPE = new SwipeTableDef();

    public static class SwipeTableDef extends TableDef {
        public final QueryColumn SEQUENCE = new QueryColumn(this, "sequence");

        public final QueryColumn IMAGE_URL = new QueryColumn(this, "image_url");

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        public final QueryColumn[] DEFAULT_COLUMNS = {SEQUENCE, IMAGE_URL};

        private SwipeTableDef() {
            super("", "swipe");
        }

        private SwipeTableDef(String schema, String name, String alias) {
            super(schema, name, alias);
        }

        public SwipeTableDef as(String alias) {
            var key = getNameWithSchema() + "." + alias;
            return getCache(key, (k) -> new SwipeTableDef("", "swipe", alias));
        }
    }

}
