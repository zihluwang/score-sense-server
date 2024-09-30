package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.entity.User;
import com.ahgtgk.scoresense.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        userRepository.insert(user);
    }

}
