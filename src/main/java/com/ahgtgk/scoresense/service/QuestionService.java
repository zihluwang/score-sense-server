package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.entity.Question;
import com.ahgtgk.scoresense.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * 根据考试 ID 删除问题。
     *
     * @param examId 考试 ID
     * @return 被删除的行数
     */
    public int deleteByExamId(Long examId) {
        return questionRepository.deleteByCondition(Question.QUESTION.EXAM_ID.eq(examId));
    }

}
