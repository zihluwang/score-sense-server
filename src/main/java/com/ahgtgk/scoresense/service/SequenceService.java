package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.repository.SequenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SequenceService {

    private final SequenceRepository sequenceRepository;

    @Autowired
    public SequenceService(SequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    @Transactional
    public Long next(String key) {
        // 查询 key 对应的 next 值并锁定行
        var nextValue = sequenceRepository.selectNextByKey(key);

        if (nextValue == null) {
            // 如果 key 不存在，插入初始值 1，并返回 1
            var initialValue = 1L;
            sequenceRepository.insertNext(key, initialValue + 1);
            return initialValue;
        } else {
            // 如果 key 存在，更新 next 值并返回当前值
            sequenceRepository.updateNextByKey(key);
            return nextValue;
        }
    }

}
