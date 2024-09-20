package com.ronglankj.scoresense.service;

import com.ronglankj.scoresense.entity.Sequence;
import com.ronglankj.scoresense.repository.SequenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class SequenceService {

    private final SequenceRepository repository;

    private final Sequence.SequenceTableDef SEQUENCE = Sequence.SEQUENCE;

    @Autowired
    public SequenceService(SequenceRepository repository) {
        this.repository = repository;
    }

    /**
     * 获取序号。
     *
     * @param key 序号 key
     * @return 下一个可用序号，如果数据库中没有该 key 则返回 1
     */
    public Integer getNextSequence(String key) {
        var seq = repository.selectOneByCondition(SEQUENCE.KEY.eq(key));
        var result = Optional.ofNullable(seq)
                .map(Sequence::getNextSequence)
                .orElse(1);
        increase(key);
        return result;
    }

    /**
     * 删除最大的序号。
     *
     * @param key 序号 key
     * @return 删除最大序号后的下一个可用序号
     */
    public Integer releaseLargestSequence(String key) {
        var seq = repository.selectOneByCondition(SEQUENCE.KEY.eq(key));
        if (Objects.isNull(seq)) {
            return -1;
        }
        decrease(key);
        return seq.getNextSequence();
    }

    /**
     * 添加序号。如果没有该序号则设置为默认值 1。
     *
     * @param key 序号 key
     * @return 被影响的行数
     */
    private int increase(String key) {
        var seq = repository.selectOneByCondition(SEQUENCE.KEY.eq(key));
        if (Objects.isNull(seq)) { // sequence 为空，保存新序号
            seq = Sequence.builder()
                    .key(key)
                    .nextSequence(1)
                    .build();
            return repository.insert(seq);
        } else {
            seq.setNextSequence(seq.getNextSequence() + 1);
            return repository.update(seq);
        }
    }

    /**
     * 减小序号。
     *
     * @param key 序号 key
     * @return 受影响的行数
     */
    private int decrease(String key) {
        var seq = repository.selectOneByCondition(SEQUENCE.KEY.eq(key));
        if (Objects.isNull(seq)) { // Sequence 为空，无法进行减少
            return 0;
        }

        seq.setNextSequence(seq.getNextSequence() - 1);
        return repository.update(seq);
    }
}
