package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.domain.UserDomain;
import com.ahgtgk.scoresense.entity.User;
import com.ahgtgk.scoresense.exception.BizException;
import com.ahgtgk.scoresense.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void createUser(User user) {
        userRepository.insert(user);
    }

    /**
     * 获取当前用户。
     */
    public UserDomain getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDomain currentUser) {
            return currentUser;
        }
        throw new BizException(HttpStatus.UNAUTHORIZED, "无法获取用户身份信息");
    }

}
