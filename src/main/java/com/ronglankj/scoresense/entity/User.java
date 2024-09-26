package com.ronglankj.scoresense.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;
import com.ronglankj.scoresense.model.payload.UserPayload;
import com.ronglankj.scoresense.view.UserView;
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
    private Long avatarId;

    /**
     * 用户账户是否被封禁。
     */
    private Boolean isBlocked;

    public static final UserTableDef USER = new UserTableDef();

    public static class UserTableDef extends TableDef {

        public final QueryColumn ID = new QueryColumn(this, "id");

        public final QueryColumn OPEN_ID = new QueryColumn(this, "open_id");

        public final QueryColumn USERNAME = new QueryColumn(this, "username");

        public final QueryColumn PHONE_NUMBER = new QueryColumn(this, "phone_number");

        public final QueryColumn AVATAR_ID = new QueryColumn(this, "avatar_id");

        public final QueryColumn IS_BLOCKED = new QueryColumn(this, "is_blocked");

        public final QueryColumn[] DEFAULT_COLUMNS = {ID, OPEN_ID, USERNAME, PHONE_NUMBER, AVATAR_ID, IS_BLOCKED};

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

    /**
     * 将持久层对象转换为视图层对象。
     *
     * @return 用户视图
     */
    public UserView toView() {
        return UserView.builder()
                .id(id)
                .openId(openId)
                .username(username)
                .phoneNumber(phoneNumber)
                .avatarId(avatarId)
                .isBlocked(isBlocked)
                .build();
    }

    /**
     * 将实体类转换为 Token 中的载荷。
     *
     * @return Token 中的载荷
     */
    public UserPayload toPayload() {
        return UserPayload.builder()
                .id(id)
                .openId(openId)
                .build();
    }

}
