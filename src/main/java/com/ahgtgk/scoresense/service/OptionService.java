package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.entity.Option;
import com.ahgtgk.scoresense.repository.OptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OptionService {

    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public int deleteByExamId(Long examId) {
        return optionRepository.deleteByCondition(Option.OPTION.EXAM_ID.eq(examId));
    }

}
