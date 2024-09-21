package com.ronglankj.scoresense.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
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

    /**
     * 图片 ID。
     */
    @Id(keyType = KeyType.None)
    private Long id;

    /**
     * 图片名称。
     */
    private String name;

    /**
     * 轮播图次序。
     */
    private Integer sequence;

    /**
     * 图片 URL。
     */
    private String imageUrl;

    public static final SwipeTableDef SWIPE = new SwipeTableDef();

    public static class SwipeTableDef extends TableDef {
        public final QueryColumn ID = new QueryColumn(this, "id");

        public final QueryColumn NAME = new QueryColumn(this, "name");

        public final QueryColumn SEQUENCE = new QueryColumn(this, "sequence");

        public final QueryColumn IMAGE_URL = new QueryColumn(this, "image_url");

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        public final QueryColumn[] DEFAULT_COLUMNS = {ID, NAME, SEQUENCE, IMAGE_URL};

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
