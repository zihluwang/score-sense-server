package com.ahgtgk.scoresense.service;

import com.onixbyte.guid.GuidCreator;
import com.ahgtgk.scoresense.entity.Admin;
import com.ahgtgk.scoresense.exception.InvalidAuthenticationException;
import com.ahgtgk.scoresense.exception.PropertyExistedException;
import com.ahgtgk.scoresense.repository.AdminRepository;
import com.ahgtgk.scoresense.security.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final GuidCreator<Long> adminIdCreator;

    @Autowired
    public AdminService(AdminRepository adminRepository,
                        PasswordEncoder passwordEncoder,
                        @Qualifier("adminIdCreator") GuidCreator<Long> adminIdCreator) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminIdCreator = adminIdCreator;
    }

    /**
     * 管理员登录功能。
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的管理员
     */
    public Admin login(String username, String password) {
        var admin = adminRepository.selectOneByCondition(Admin.ADMIN.USERNAME.eq(username));
        if (Objects.isNull(admin)) {
            throw new InvalidAuthenticationException();
        }

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new InvalidAuthenticationException();
        }
        return admin;
    }

    public Admin createAdmin(String username, String password) {
        var admin = Admin.builder()
                .id(adminIdCreator.nextId())
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        var canCreate = adminRepository.selectCountByCondition(Admin.ADMIN.USERNAME.eq(username)) == 0;
        if (!canCreate) {
            throw new PropertyExistedException("管理员用户名", username);
        }

        adminRepository.insert(admin);
        return admin;
    }

}
