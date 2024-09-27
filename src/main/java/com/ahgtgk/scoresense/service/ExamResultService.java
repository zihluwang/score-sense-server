package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.entity.ExamResult;
import com.ahgtgk.scoresense.repository.ExamResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExamResultService {

    private final ExamResultRepository examResultRepository;

    public ExamResultService(ExamResultRepository examResultRepository) {
        this.examResultRepository = examResultRepository;
    }

    /**
     * 根据考试 ID 删除用户的考试结果。
     *
     * @param examId 考试 ID
     * @return 用户的考试结果
     */
    public int deleteByExamId(Long examId) {
        return examResultRepository.deleteByCondition(ExamResult.EXAM_RESULT.EXAM_ID.eq(examId));
    }

}
