package com.ronglankj.scoresense.entity;

import lombok.*;

/**
 * 用户信息。
 *
 * @author zihluwang
 */
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

}
