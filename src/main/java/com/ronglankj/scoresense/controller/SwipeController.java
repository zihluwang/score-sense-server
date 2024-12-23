package com.ronglankj.scoresense.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.ronglankj.scoresense.entity.Swipe;
import com.ronglankj.scoresense.enumeration.SwipeStatus;
import com.ronglankj.scoresense.exception.BaseBizException;
import com.ronglankj.scoresense.model.request.CreateSwipeRequest;
import com.ronglankj.scoresense.model.request.UpdateSwipeRequest;
import com.ronglankj.scoresense.model.response.ActionResponse;
import com.ronglankj.scoresense.service.SwipeService;
import com.ronglankj.scoresense.util.DateTimeUtils;
import com.ronglankj.scoresense.view.SwipeView;
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
    public List<SwipeView> getAvailableSwipes() {
        return swipeService.getAvailableSwipes().stream()
                .map(Swipe::toView)
                .toList();
    }

    @GetMapping("/")
    public Page<SwipeView> getSwipes(@RequestParam(defaultValue = "1") Integer currentPage,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) SwipeStatus status) {
        return swipeService.getSwipes(currentPage, pageSize, name, status)
                .map(Swipe::toView);
    }

    @PostMapping("/")
    public SwipeView createSwipe(@RequestBody CreateSwipeRequest request) {
        if (Objects.isNull(request.imageId())) {
            throw new BaseBizException(HttpStatus.BAD_REQUEST, "图片ID不能为空！");
        }
        return swipeService.addSwipe(request.name(), request.status(), request.imageId()).toView();
    }

    @PatchMapping("/")
    public SwipeView updateSwipe(@RequestBody UpdateSwipeRequest request) {
        return swipeService.updateSwipe(request).toView();
    }

    @DeleteMapping("/{swipeId}")
    public ActionResponse deleteSwipe(@PathVariable Long swipeId) {
        swipeService.removeSwipe(swipeId);
        return ActionResponse.builder()
                .timestamp(DateTimeUtils.toInstant(LocalDateTime.now()))
                .message("删除成功。").build();
    }

}
