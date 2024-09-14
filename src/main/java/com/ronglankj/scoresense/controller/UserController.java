package com.ronglankj.scoresense.controller;

import com.ronglankj.scoresense.model.request.UserLoginOrRegisterRequest;
import com.ronglankj.scoresense.view.UserView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/login-or-register")
    public ResponseEntity<UserView> loginOrRegister(@RequestBody UserLoginOrRegisterRequest request) {
        var token = "";
        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", token)
                .body(null);
    }

}
