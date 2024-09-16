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

}
