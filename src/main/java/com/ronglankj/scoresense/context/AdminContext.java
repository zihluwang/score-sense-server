package com.ronglankj.scoresense.context;

import com.ronglankj.scoresense.entity.Admin;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 管理员上下文信息
 */
@Getter
@Setter
@Builder
public class AdminContext {

    private Admin admin;

}
