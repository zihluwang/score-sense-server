package com.ronglankj.scoresense.controller;

import com.ronglankj.scoresense.entity.Swipe;
import com.ronglankj.scoresense.service.SwipeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/swipes")
public class SwipeController {

    private final SwipeService swipeService;

    public SwipeController(SwipeService swipeService) {
        this.swipeService = swipeService;
    }

    @GetMapping("/")
    public List<Swipe> getSwipes() {
        return swipeService.getSwipes();
    }

}
