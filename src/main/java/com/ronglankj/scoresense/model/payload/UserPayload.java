package com.ronglankj.scoresense.model.payload;

import com.onixbyte.simplejwt.TokenPayload;
import com.ronglankj.scoresense.entity.User;
import lombok.*;

/**
 * 用户载荷信息。
 *
 * @author zihluwang
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserPayload implements TokenPayload {

    /**
     * 用户 ID。
     */
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

    /**
     * 用户是否被封禁。
     */
    private Boolean isBlocked;

    /**
     * 将在和信息转换为持久层对象。
     *
     * @return 用户持久层对象
     */
    public User toPersistent() {
        return User.builder()
                .id(id)
                .openId(openId)
                .username(username)
                .phoneNumber(phoneNumber)
                .avatarUrl(avatarUrl)
                .isBlocked(isBlocked)
                .build();
    }

}
