package com.ahgtgk.scoresense.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import com.ahgtgk.scoresense.view.AttachmentView;
import lombok.*;

@Table("attachment")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Attachment {

    @Id(keyType = KeyType.None)
    private Long id;

    private String name;

    private String path;

    private String contentType;

    public static final AttachmentTableDef ATTACHMENT = new AttachmentTableDef();

    public static class AttachmentTableDef extends TableDef {
        public final QueryColumn ID = new QueryColumn(this, "id");

        public final QueryColumn NAME = new QueryColumn(this, "name");

        public final QueryColumn PATH = new QueryColumn(this, "path");

        public final QueryColumn CONTENT_TYPE = new QueryColumn(this, "content_type");

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        public final QueryColumn[] DEFAULT_COLUMNS = {ID, NAME, PATH, CONTENT_TYPE};

        private AttachmentTableDef() {
            super("", "attachment");
        }

        private AttachmentTableDef(String schema, String tableName, String alias) {
            super(schema, tableName, alias);
        }

        public AttachmentTableDef as(String alias) {
            var key = getNameWithSchema() + "." + alias;
            return getCache(key, (k) -> new AttachmentTableDef("", "attachment", alias));
        }
    }

    public AttachmentView toView() {
        return AttachmentView.builder()
                .id(String.valueOf(id))
                .name(name)
                .contentType(contentType)
                .path(path)
                .build();
    }

}
