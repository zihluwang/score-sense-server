package com.ronglankj.scoresense.controller;

import com.mybatisflex.core.paginate.Page;
import com.ronglankj.scoresense.entity.Swipe;
import com.ronglankj.scoresense.exception.BaseBizException;
import com.ronglankj.scoresense.model.request.CreateSwipeRequest;
import com.ronglankj.scoresense.model.request.UpdateSwipeRequest;
import com.ronglankj.scoresense.model.response.ActionResponse;
import com.ronglankj.scoresense.service.SwipeService;
import com.ronglankj.scoresense.util.DateTimeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/swipes")
public class SwipeController {

    private final SwipeService swipeService;

    public SwipeController(SwipeService swipeService) {
        this.swipeService = swipeService;
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

    @PatchMapping("/")
    public Swipe updateSwipe(@RequestBody UpdateSwipeRequest request) {
        return swipeService.updateSwipe(request);
    }

    @DeleteMapping("/{swipeId}")
    public ActionResponse deleteSwipe(@PathVariable Long swipeId) {
        swipeService.removeSwipe(swipeId);
        return ActionResponse.builder()
                .timestamp(DateTimeUtils.toInstant(LocalDateTime.now()))
                .message("删除成功。").build();
    }

}
