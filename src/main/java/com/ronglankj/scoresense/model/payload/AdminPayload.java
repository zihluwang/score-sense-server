package com.ronglankj.scoresense.model.payload;

import com.onixbyte.simplejwt.TokenPayload;
import com.ronglankj.scoresense.entity.Admin;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AdminPayload implements TokenPayload {

    private String id;

    private String username;

    public Admin toPersistent() {
        return Admin.builder()
                .id(Long.parseLong(id))
                .username(username)
                .build();
    }

}
