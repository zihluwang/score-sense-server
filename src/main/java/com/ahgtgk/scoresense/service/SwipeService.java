package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.entity.Swipe;
import com.ahgtgk.scoresense.enumeration.Status;
import com.ahgtgk.scoresense.exception.DataConflictException;
import com.ahgtgk.scoresense.model.criteria.SearchSwipeCriteria;
import com.ahgtgk.scoresense.model.request.CreateSwipeRequest;
import com.ahgtgk.scoresense.model.request.UpdateSwipeRequest;
import com.ahgtgk.scoresense.repository.SwipeRepository;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import com.onixbyte.guid.GuidCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class SwipeService {

    private final SwipeRepository swipeRepository;
    private final GuidCreator<Long> swipeIdCreator;

    public SwipeService(SwipeRepository swipeRepository, GuidCreator<Long> swipeIdCreator) {
        this.swipeRepository = swipeRepository;
        this.swipeIdCreator = swipeIdCreator;
    }

    /**
     * 分页查询轮播图数据。
     *
     * @param currentPage 当前页码
     * @param pageSize    页面数据量
     * @param criteria    查询条件
     */
    public Page<Swipe> getSwipes(Integer currentPage, Integer pageSize, SearchSwipeCriteria criteria) {
        var queryWrapper = QueryWrapper.create();

        // 配置轮播图名称查询条件
        if (Objects.nonNull(criteria.name()) && !criteria.name().isBlank()) {
            queryWrapper.and(Swipe.SWIPE.NAME.like(criteria.name()));
        }

        // 配置轮播图状态查询条件
        if (Objects.nonNull(criteria.status())) {
            queryWrapper.and(Swipe.SWIPE.STATUS.eq(criteria.status()));
        }

        // 设置轮播图顺序
        queryWrapper.orderBy(Swipe.SWIPE.ID, false);

        return swipeRepository.paginate(currentPage, pageSize, queryWrapper);
    }

    /**
     * 根据轮播图 ID 查询轮播图详细信息。
     *
     * @param swipeId 轮播图 ID
     */
    public Swipe getSwipe(Long swipeId) {
        return swipeRepository.selectOneById(swipeId);
    }

    /**
     * 创建轮播图。若轮播图状态未设置，将会使用 {@link Status#DISABLED} 作为默认值。
     *
     * @param request 创建轮播图请求
     */
    public Swipe createSwipe(CreateSwipeRequest request) {
        var canCreate = swipeRepository.selectCountByCondition(Swipe.SWIPE.NAME.eq(request.name())) == 0;
        if (!canCreate) {
            throw new DataConflictException("轮播图名称");
        }

        var swipe = Swipe.builder()
                .id(swipeIdCreator.nextId())
                .name(request.name())
                .imageId(request.imageId())
                .status(Optional.ofNullable(request.status())
                        .orElse(Status.DISABLED))
                .build();

        swipeRepository.insert(swipe);
        return swipe;
    }

    /**
     * 忽略 null 值更新轮播图信息。
     *
     * @param updateSwipeRequest 更新轮播图请求
     */
    public Swipe updateSwipe(UpdateSwipeRequest updateSwipeRequest) {
        var swipe = UpdateEntity.of(Swipe.class, updateSwipeRequest.id());

        // 设置轮播图名称
        if (Objects.nonNull(updateSwipeRequest.name()) && !updateSwipeRequest.name().isBlank()) {
            swipe.setName(updateSwipeRequest.name());
        }

        // 设置轮播图图片 ID
        if (Objects.nonNull(updateSwipeRequest.imageId())) {
            swipe.setImageId(updateSwipeRequest.imageId());
        }

        // 设置轮播图状态
        if (Objects.nonNull(updateSwipeRequest.status())) {
            swipe.setStatus(updateSwipeRequest.status());
        }

        // 执行更新
        swipeRepository.update(swipe);
        return swipe;
    }

    public void deleteSwipe(Long swipeId) {
        swipeRepository.deleteById(swipeId);
    }

}
