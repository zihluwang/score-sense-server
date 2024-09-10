package com.ronglankj.scoresense.entity;

import lombok.*;

/**
 * 管理员。
 *
 * @author zihluwang
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Admin {

    private Long id;

    private String username;

    private String password;

}
