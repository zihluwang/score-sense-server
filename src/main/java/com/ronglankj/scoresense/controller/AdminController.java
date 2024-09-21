package com.ronglankj.scoresense.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.onixbyte.guid.GuidCreator;
import com.onixbyte.guid.impl.SnowflakeGuidCreator;
import com.onixbyte.simplejwt.TokenResolver;
import com.ronglankj.scoresense.entity.Admin;
import com.ronglankj.scoresense.model.request.AdminLoginRequest;
import com.ronglankj.scoresense.model.request.CreateAdminRequest;
import com.ronglankj.scoresense.security.PasswordEncoder;
import com.ronglankj.scoresense.service.AdminService;
import com.ronglankj.scoresense.view.AdminView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/admins/")
public class AdminController {

    private final TokenResolver<DecodedJWT> tokenResolver;
    private final AdminService adminService;

    @Autowired
    public AdminController(TokenResolver<DecodedJWT> tokenResolver,
                           AdminService adminService) {
        this.tokenResolver = tokenResolver;
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<AdminView> login(@RequestBody AdminLoginRequest request) {
        var admin = adminService.login(request.username(), request.password());
        var token = tokenResolver.createToken(Duration.ofHours(3), admin.getUsername(), "ScoreSense-Admin", admin.toPayload());
        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", token)
                .body(admin.toView());
    }

    @PostMapping("/")
    public AdminView createAdmin(@RequestBody CreateAdminRequest request) {
        var admin = adminService.createAdmin(request.username(), request.password());
        return admin.toView();
    }

}
