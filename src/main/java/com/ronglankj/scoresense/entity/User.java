package com.ronglankj.scoresense.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import lombok.*;

/**
 * 用户信息。
 *
 * @author zihluwang
 */
@Table("user")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {

    /**
     * 用户 ID。
     */
    @Id(keyType = KeyType.None)
    private Long id;

    /**
     * 微信开放平台用户 ID。
     */
    private String openId;

    /**
     * 用户名，即微信昵称。
     */
    private String username;

    /**
     * 用户手机号码。
     */
    private String phoneNumber;

    /**
     * 用户头像 URL。
     */
    private String avatarUrl;

    public static final UserTableDef USER = new UserTableDef();

    public static class UserTableDef extends TableDef {

        public final QueryColumn ID = new QueryColumn(this, "id");

        public final QueryColumn OPEN_ID = new QueryColumn(this, "open_id");

        public final QueryColumn USERNAME = new QueryColumn(this, "username");

        public final QueryColumn PHONE_NUMBER = new QueryColumn(this, "phone_number");

        public final QueryColumn AVATAR_URL = new QueryColumn(this, "avatar_url");

        public final QueryColumn[] DEFAULT_COLUMNS = {ID, OPEN_ID, USERNAME, PHONE_NUMBER, AVATAR_URL};

        public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

        private UserTableDef() {
            super("", "user");
        }

        private UserTableDef(String schema, String name, String alias) {
            super(schema, name, alias);
        }

        public UserTableDef as(String alias) {
            var key = getNameWithSchema() + "." + alias;
            return getCache(key, (k) -> new UserTableDef("", "user", alias));
        }
    }

}
