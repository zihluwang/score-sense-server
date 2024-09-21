package com.ronglankj.scoresense.controller;

import com.mybatisflex.core.paginate.Page;
import com.ronglankj.scoresense.entity.Swipe;
import com.ronglankj.scoresense.exception.BaseBizException;
import com.ronglankj.scoresense.model.request.CreateSwipeRequest;
import com.ronglankj.scoresense.service.SequenceService;
import com.ronglankj.scoresense.service.SwipeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/swipes")
public class SwipeController {

    private final SwipeService swipeService;
    private final SequenceService sequenceService;

    public SwipeController(SwipeService swipeService, SequenceService sequenceService) {
        this.swipeService = swipeService;
        this.sequenceService = sequenceService;
    }

    @GetMapping("/enabled")
    public List<Swipe> getAvailableSwipes() {
        return swipeService.getAvailableSwipes();
    }

    @GetMapping("/")
    public Page<Swipe> getSwipes(@RequestParam(defaultValue = "1") Integer currentPage,
                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        return swipeService.getSwipes(currentPage, pageSize);
    }

    @PostMapping("/")
    public Swipe createSwipe(@RequestBody CreateSwipeRequest request) {
        if (Objects.isNull(request.imageUrl()) || request.imageUrl().isBlank()) {
            throw new BaseBizException(HttpStatus.BAD_REQUEST, "图片链接不能为空！");
        }
        return swipeService.addSwipe(request.name(), request.sequence(), request.imageUrl());
    }

}
