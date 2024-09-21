package com.ronglankj.scoresense.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import com.onixbyte.guid.GuidCreator;
import com.ronglankj.scoresense.config.ConcurrentConfig;
import com.ronglankj.scoresense.entity.Swipe;
import com.ronglankj.scoresense.exception.BaseBizException;
import com.ronglankj.scoresense.model.request.UpdateSwipeRequest;
import com.ronglankj.scoresense.repository.SwipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class SwipeService {

    private final SwipeRepository swipeRepository;
    private final SequenceService sequenceService;
    private final GuidCreator<Long> swipeIdCreator;

    @Autowired
    public SwipeService(SwipeRepository swipeRepository,
                        SequenceService sequenceService,
                        @Qualifier("swipeIdCreator") GuidCreator<Long> swipeIdCreator) {
        this.swipeRepository = swipeRepository;
        this.sequenceService = sequenceService;
        this.swipeIdCreator = swipeIdCreator;
    }

    private final String SEQUENCE_KEY = "SWIPE";

    private final Swipe.SwipeTableDef SWIPE = Swipe.SWIPE;

    /**
     * 添加轮播图。
     *
     * @param name     图片名称
     * @param sequence 图片次序，{@code null} 时自动填充为最后的次序
     * @param imageUrl 图片 URL
     * @return 被存储的轮播图
     */
    public Swipe addSwipe(String name, Integer sequence, String imageUrl) {
        var swipeId = swipeIdCreator.nextId();
        var swipe = Swipe.builder()
                .id(swipeId)
                .name(Optional.ofNullable(name).filter(String::isBlank).orElse("图片-%d".formatted(swipeId)))
                .sequence(Optional.ofNullable(sequence).orElse(sequenceService.getNextSequence(SEQUENCE_KEY)))
                .imageUrl(imageUrl)
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
        // 获取轮播图信息
        var swipe = swipeRepository.selectOneByCondition(SWIPE.ID.eq(id));
        if (Objects.isNull(swipe)) {
            throw new BaseBizException(HttpStatus.BAD_REQUEST, "没有相应记录");
        }

        // 检测轮播图是否启用
        if (Objects.nonNull(swipe.getSequence())) { // 轮播图已启用
            // 更新轮播图次序
            swipeRepository.updateSequenceAfter(swipe.getSequence());
        }

        // 删除轮播图
        swipeRepository.deleteByCondition(SWIPE.ID.eq(swipe.getId()));
        sequenceService.releaseLargestSequence(SEQUENCE_KEY);
        return 1;
    }

    /**
     * 修改轮播图序列。
     */
    public void updateSwipeSequence(List<Swipe> swipes) {
        // 清空当前轮播图顺序
        swipeRepository.removeAllSequences();
        // 初始化序号
        var index = new AtomicInteger();
        var updateTasks = swipes.stream().map((swipe) -> {
            swipe.setSequence(index.get() + 1);
            index.getAndIncrement();
            return CompletableFuture.runAsync(
                    () -> swipeRepository.updateByCondition(swipe, SWIPE.ID.eq(swipe.getId())),
                    ConcurrentConfig.CACHED_EXECUTORS
            );
        }).toArray(CompletableFuture[]::new);
        // 等待所有的更新任务完成
        CompletableFuture.allOf(updateTasks).join();
    }

    /**
     * 获取所有轮播图。
     *
     * @return 所有轮播图信息
     */
    public List<Swipe> getAvailableSwipes() {
        return swipeRepository.selectListByCondition(SWIPE.SEQUENCE.isNotNull());
    }

    /**
     * 获取轮播图分页数据。
     *
     * @param currentPage 当前页码
     * @param pageSize    页面数据量
     * @return 轮播图分页数据
     */
    public Page<Swipe> getSwipes(Integer currentPage, Integer pageSize) {
        return swipeRepository.paginate(currentPage, pageSize, QueryWrapper.create()
                .orderBy(SWIPE.ID, true));
    }

    public Swipe updateSwipe(UpdateSwipeRequest request) {
        var swipe = UpdateEntity.of(Swipe.class, request.id());
        Optional.ofNullable(request.name())
                .ifPresent(swipe::setName);
        Optional.ofNullable(request.sequence())
                .ifPresent(swipe::setSequence);
        Optional.ofNullable(request.imageUrl())
                .ifPresent(swipe::setImageUrl);
        var updatedRowCount = swipeRepository.update(swipe);
        if (updatedRowCount > 0) {
            return swipeRepository.selectOneByCondition(SWIPE.ID.eq(request.id()));
        } else {
            return null;
        }
    }

}
