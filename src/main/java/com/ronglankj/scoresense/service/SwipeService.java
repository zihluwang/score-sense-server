package com.ronglankj.scoresense.service;

import com.ronglankj.scoresense.entity.Swipe;
import com.ronglankj.scoresense.repository.SwipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SwipeService {

    private final SwipeRepository swipeRepository;
    private final SequenceService sequenceService;

    @Autowired
    public SwipeService(SwipeRepository swipeRepository, SequenceService sequenceService) {
        this.swipeRepository = swipeRepository;
        this.sequenceService = sequenceService;
    }

    private final String SEQUENCE_KEY = "SWIPE";

    private final Swipe.SwipeTableDef SWIPE = Swipe.SWIPE;

    /**
     * 添加轮播图。
     *
     * @param imageUrl 图片 URL
     * @return 影响的行数
     */
    public int addSwipe(String imageUrl) {
        return swipeRepository.insert(Swipe.builder()
                .sequence(sequenceService.getNextSequence(SEQUENCE_KEY))
                .imageUrl(imageUrl)
                .build());
    }

    /**
     * 移除最后一张轮播图。
     *
     * @return 影响的行数
     */
    public int removeSwipe() {
        // 获取最大序列号，为下一可用序列号 - 1
        var maxSequence = sequenceService.getNextSequence(SEQUENCE_KEY) - 1;
        // 删除轮播图
        swipeRepository.deleteByCondition(SWIPE.SEQUENCE.eq(maxSequence));
        // 序列号自减
        sequenceService.releaseLargestSequence(SEQUENCE_KEY);
        return 1;
    }

    /**
     * 替换指定顺序的轮播图。
     *
     * @param sequence 图片序号
     * @param imageUrl 新图片地址
     * @return 受影响的行数
     */
    public int replaceSwipe(Integer sequence, String imageUrl) {
        var isSwipeExistent = swipeRepository.selectCountByCondition(SWIPE.SEQUENCE.eq(sequence)) == 1;
        if (isSwipeExistent) {
            return swipeRepository.update(Swipe.builder()
                    .sequence(sequence)
                    .imageUrl(imageUrl)
                    .build());
        } else {
            return 0;
        }
    }

    /**
     * 获取所有轮播图。
     *
     * @return 所有轮播图信息
     */
    public List<Swipe> getSwipes() {
        return swipeRepository.selectAll();
    }

}
