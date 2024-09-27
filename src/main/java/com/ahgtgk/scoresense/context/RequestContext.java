package com.ahgtgk.scoresense.context;

import lombok.*;

/**
 * 请求上下文。
 *
 * @author zihluwang
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestContext {

    private String requestId;

}
