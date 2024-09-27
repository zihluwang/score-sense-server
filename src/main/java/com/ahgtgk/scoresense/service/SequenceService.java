package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.entity.Sequence;
import com.ahgtgk.scoresense.repository.SequenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class SequenceService {

    private final SequenceRepository sequenceRepository;

    public SequenceService(SequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    public Long next(String key) {
        final var condition = Sequence.SEQUENCE.KEY.eq(key);
        var sequence = sequenceRepository.selectOneByCondition(condition);

        if (Objects.isNull(sequence)) {
            sequence = Sequence.builder()
                    .key(key)
                    .next(1L)
                    .build();
            sequenceRepository.insert(sequence);
        }

        var next = sequence.getNext();
        sequence.setNext(next + 1);
        sequenceRepository.update(sequence);
        return next;
    }

    public void previous(String key) {
        final var condition = Sequence.SEQUENCE.KEY.eq(key);
        var sequence = sequenceRepository.selectOneByCondition(condition);
        if (Objects.nonNull(sequence)) {
            var _next = sequence.getNext() - 1;
            sequence.setNext(_next);
            sequenceRepository.updateByCondition(sequence, condition);
        }
    }

}
