package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.entity.Answer;
import com.ahgtgk.scoresense.entity.ExamResult;
import com.ahgtgk.scoresense.repository.ExamResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExamResultService {

    private final ExamResultRepository examResultRepository;
    private final UserService userService;
    private final AnswerService answerService;

    public ExamResultService(ExamResultRepository examResultRepository, UserService userService, AnswerService answerService, QuestionService questionService) {
        this.examResultRepository = examResultRepository;
        this.userService = userService;
        this.answerService = answerService;
    }

    /**
     * 完成考试。
     *
     * @param examId    考试 ID
     * @param vacancyId 岗位 ID
     */
    public ExamResult finishExam(Long examId, Long vacancyId) {
        var currentUser = userService.getCurrentUser();

        // 获取用户的答题信息
        var answers = answerService.getAnswers(examId);

        // 补充用户未回答的题目
        answerService.fillUnansweredQuestions(examId, answers);

        // 计算总分
        var totalScore = answers.stream()
                .mapToInt(Answer::getScore)
                .sum();

        // 构建考试结果信息
        var examResult = ExamResult.builder()
                .examId(examId)
                .userId(currentUser.getId())
                .vacancyId(vacancyId)
                .totalScore(totalScore)
                .build();
        examResultRepository.insertOrUpdate(examResult);
        return examResult;
    }

}
