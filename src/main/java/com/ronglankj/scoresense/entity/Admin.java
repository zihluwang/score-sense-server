package com.ronglankj.scoresense.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import com.ronglankj.scoresense.model.payload.AdminPayload;
import com.ronglankj.scoresense.view.AdminView;
import lombok.*;

/**
 * 管理员。
 *
 * @author zihluwang
 */
@Table("admin")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Admin {

    @Id(keyType = KeyType.None)
    private Long id;

    private String username;

    private String password;

    public static final AdminTableDef ADMIN = new AdminTableDef();

    public static class AdminTableDef extends TableDef {
        public final QueryColumn ID = new QueryColumn(this, "id");

        public final QueryColumn USERNAME = new QueryColumn(this, "username");

        public final QueryColumn PASSWORD = new QueryColumn(this, "password");

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        public final QueryColumn[] DEFAULT_COLUMNS = {ID, USERNAME, PASSWORD};

        private AdminTableDef() {
            super("", "admin");
        }

        private AdminTableDef(String schema, String name, String alias) {
            super(schema, name, alias);
        }

        public AdminTableDef as(String alias) {
            var key = getNameWithSchema() + "." + alias;
            return getCache(key, (k) -> new AdminTableDef("", "admin", alias));
        }
    }

    public AdminView toView() {
        return AdminView.builder()
                .id(id)
                .username(username)
                .build();
    }

    public AdminPayload toPayload() {
        return AdminPayload.builder()
                .id(String.valueOf(id))
                .username(username)
                .build();
    }

}
