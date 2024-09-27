package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.entity.Answer;
import com.ahgtgk.scoresense.repository.AnswerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    /**
     * 根据考试 ID 删除用户的回答。
     *
     * @param examId 考试 ID
     * @return 被删除的行数
     */
    public int deleteByExamId(Long examId) {
        return answerRepository.deleteByCondition(Answer.ANSWER.EXAM_ID.eq(examId));
    }

}
