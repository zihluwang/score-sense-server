package com.ronglankj.scoresense.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import com.onixbyte.guid.GuidCreator;
import com.ronglankj.scoresense.entity.Swipe;
import com.ronglankj.scoresense.enumeration.SwipeStatus;
import com.ronglankj.scoresense.exception.BaseBizException;
import com.ronglankj.scoresense.model.request.UpdateSwipeRequest;
import com.ronglankj.scoresense.repository.SwipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class SwipeService {

    private final SwipeRepository swipeRepository;
    private final GuidCreator<Long> swipeIdCreator;

    @Autowired
    public SwipeService(SwipeRepository swipeRepository,
                        @Qualifier("swipeIdCreator") GuidCreator<Long> swipeIdCreator) {
        this.swipeRepository = swipeRepository;
        this.swipeIdCreator = swipeIdCreator;
    }

    private final Swipe.SwipeTableDef SWIPE = Swipe.SWIPE;

    /**
     * 添加轮播图。
     *
     * @param name    图片名称
     * @param status  图片状态
     * @param imageId 图片 ID
     * @return 被存储的轮播图
     */
    public Swipe addSwipe(String name, SwipeStatus status, Long imageId) {
        // 生成图片 ID
        var swipeId = swipeIdCreator.nextId();

        // 构建实体数据
        var swipe = Swipe.builder()
                .id(swipeId)
                .name(Optional.ofNullable(name)
                        .filter((_name) -> !_name.isBlank())
                        .orElse("图片-%d".formatted(swipeId)))
                .status(status)
                .imageId(imageId)
                .build();
        swipeRepository.insert(swipe);
        return swipe;
    }

    /**
     * 移除最后一张轮播图。
     *
     * @param id 轮播图 ID
     * @return 影响的行数
     */
    public int removeSwipe(Long id) {
        return swipeRepository.deleteByCondition(SWIPE.ID.eq(id));
    }

    /**
     * 获取所有轮播图。
     *
     * @return 所有轮播图信息
     */
    public List<Swipe> getAvailableSwipes() {
        return swipeRepository.selectListByCondition(SWIPE.STATUS.eq(SwipeStatus.ENABLED));
    }

    /**
     * 获取轮播图分页数据。
     *
     * @param currentPage 当前页码
     * @param pageSize    页面数据量
     * @return 轮播图分页数据
     */
    public Page<Swipe> getSwipes(Integer currentPage, Integer pageSize, String name, SwipeStatus status) {
        var queryWrapper = QueryWrapper.create();
        Optional.ofNullable(name)
                .ifPresent((_name) -> queryWrapper.and(SWIPE.NAME.like(_name)));
        Optional.ofNullable(status)
                .ifPresent((_status) -> queryWrapper.and(SWIPE.STATUS.eq(status)));
        return swipeRepository.paginate(currentPage, pageSize, queryWrapper.orderBy(SWIPE.ID, true));
    }

    public Swipe updateSwipe(UpdateSwipeRequest request) {
        var swipe = UpdateEntity.of(Swipe.class, request.id());
        Optional.ofNullable(request.name())
                .ifPresent(swipe::setName);
        Optional.ofNullable(request.imageId())
                .ifPresent(swipe::setImageId);
        var updatedRowCount = swipeRepository.update(swipe);
        if (updatedRowCount > 0) {
            return swipeRepository.selectOneByCondition(SWIPE.ID.eq(request.id()));
        } else {
            return null;
        }
    }

}
