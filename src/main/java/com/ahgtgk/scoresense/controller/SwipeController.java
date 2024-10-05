package com.ahgtgk.scoresense.controller;

import com.ahgtgk.scoresense.entity.Swipe;
import com.ahgtgk.scoresense.enumeration.Status;
import com.ahgtgk.scoresense.model.criteria.SearchSwipeCriteria;
import com.ahgtgk.scoresense.model.request.CreateSwipeRequest;
import com.ahgtgk.scoresense.model.request.UpdateSwipeRequest;
import com.ahgtgk.scoresense.service.SwipeService;
import com.ahgtgk.scoresense.view.SwipeView;
import com.mybatisflex.core.paginate.Page;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/swipes")
public class SwipeController {

    private final SwipeService swipeService;

    public SwipeController(SwipeService swipeService) {
        this.swipeService = swipeService;
    }

    /**
     * 分页查询轮播图列表。
     *
     * @param currentPage 当前页面
     * @param pageSize    页面数据量
     * @param criteria    查询条件
     */
    @GetMapping("/")
    public Page<SwipeView> getSwipes(@RequestParam(defaultValue = "1") Integer currentPage,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @ModelAttribute SearchSwipeCriteria criteria) {
        return swipeService.getSwipes(currentPage, pageSize, criteria)
                .map(Swipe::toView);
    }

    /**
     * 查询可用的轮播图列表。
     *
     * @see SwipeService#getSwipes(Integer, Integer, SearchSwipeCriteria)
     */
    @GetMapping("/enabled")
    public List<SwipeView> getEnabledSwipes() {
        return swipeService.getSwipes(1, 500, SearchSwipeCriteria.builder()
                        .status(Status.ENABLED)
                        .build())
                .map(Swipe::toView)
                .getRecords();
    }

    @GetMapping("/{swipeId:\\d+}")
    public SwipeView getSwipe(@PathVariable Long swipeId) {
        return swipeService.getSwipe(swipeId).toView();
    }

    /**
     * 创建轮播图。
     *
     * @param request 创建轮播图请求
     */
    @PostMapping("/")
    public SwipeView createSwipe(@Valid @RequestBody CreateSwipeRequest request) {
        return swipeService.createSwipe(request).toView();
    }

    /**
     * 忽略 null 值更新轮播图信息。
     *
     * @param request 更新轮播图请求
     */
    @PatchMapping("/")
    public SwipeView updateSwipe(@Valid @RequestBody UpdateSwipeRequest request) {
        return swipeService.updateSwipe(request).toView();
    }

    /**
     * 删除轮播图。
     *
     * @param swipeId 轮播图 ID
     */
    @DeleteMapping("/{swipeId:\\d+}")
    public ResponseEntity<Void> deleteSwipe(@PathVariable Long swipeId) {
        swipeService.deleteSwipe(swipeId);
        return ResponseEntity.noContent().build();
    }

}
