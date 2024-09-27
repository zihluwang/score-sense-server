package com.ahgtgk.scoresense.context;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户上下文信息
 */
@Getter
@Setter
@Builder
public class UserContext {

    private Long id;

    private String openId;

}
