package com.ahgtgk.scoresense.domain;

import com.ahgtgk.scoresense.entity.User;
import com.ahgtgk.scoresense.enumeration.UserType;
import com.ahgtgk.scoresense.view.UserView;
import com.onixbyte.simplejwt.TokenPayload;
import com.onixbyte.simplejwt.annotations.ExcludeFromPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
public class UserDomain implements TokenPayload, UserDetails {

    private Long id;

    private String username;

    @ExcludeFromPayload
    private String password;

    private UserType userType;

    private Long avatarId;

    private String phoneNumber;

    private Boolean nonLocked;

    /**
     * 将持久层对象转换为视图层对象。
     *
     * @return 用户视图
     */
    public User toPersistent() {
        return User.builder()
                .id(id)
                .username(username)
                .phoneNumber(phoneNumber)
                .avatarId(avatarId)
                .nonLocked(nonLocked)
                .build();
    }

    /**
     * 将持久层对象转换为视图层对象。
     *
     * @return 用户视图
     */
    public UserView toView() {
        return UserView.builder()
                .id(String.valueOf(id))
                .username(username)
                .phoneNumber(phoneNumber)
                .avatarId(String.valueOf(avatarId))
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
