package com.ahgtgk.scoresense.model.payload;

import com.ahgtgk.scoresense.entity.User;
import com.ahgtgk.scoresense.enumeration.UserType;
import com.ahgtgk.scoresense.view.UserView;
import com.onixbyte.simplejwt.TokenPayload;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.relation.Role;
import java.util.Collection;
import java.util.List;

/**
 * 用户载荷信息。
 *
 * @author zihluwang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPayload implements TokenPayload, UserDetails {

    private Long id;

    private String username;

    private String password;

    private UserType userType;

    private Boolean nonLocked;

    public User toPersistent() {
        return User.builder()
                .id(id)
                .username(username)
                .password(password)
                .nonLocked(nonLocked)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userType.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonLocked() {
        return userType == UserType.ADMIN || nonLocked;
    }
}
