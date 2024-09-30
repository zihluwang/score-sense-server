package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.entity.User;
import com.ahgtgk.scoresense.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(userRepository.selectOneByCondition(User.USER.USERNAME.eq(username)))
                .map(User::toDomain)
                .orElseThrow(() -> new UsernameNotFoundException("用户身份凭证不正确"));
    }

}
