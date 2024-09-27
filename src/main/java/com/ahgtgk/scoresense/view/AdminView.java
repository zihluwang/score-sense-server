package com.ahgtgk.scoresense.view;

import lombok.Builder;

/**
 * 管理员视图
 *
 * @param id 管理员 ID
 * @param username 管理员用户名
 */
@Builder
public record AdminView(
        Long id,
        String username
) {
}
