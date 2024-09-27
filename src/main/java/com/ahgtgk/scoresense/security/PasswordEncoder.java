package com.ahgtgk.scoresense.security;

import com.onixbyte.devkit.utils.HashUtil;

/**
 * 密码编码器。
 *
 * @author zihluwang
 */
public class PasswordEncoder {

    /**
     * 将密码进行加密。
     *
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public String encode(String rawPassword) {
        return HashUtil.md5(rawPassword);
    }

    /**
     * 检查密码是否匹配。
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return 密码是否匹配
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return HashUtil.md5(rawPassword).equalsIgnoreCase(encodedPassword);
    }

}
