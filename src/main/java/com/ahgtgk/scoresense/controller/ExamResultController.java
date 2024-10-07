package com.ahgtgk.scoresense.controller;

import com.ahgtgk.scoresense.entity.Answer;
import com.ahgtgk.scoresense.entity.User;
import com.ahgtgk.scoresense.service.AnswerService;
import com.ahgtgk.scoresense.service.ExamResultService;
import com.ahgtgk.scoresense.view.AnswerView;
import com.ahgtgk.scoresense.view.ReportView;
import com.ahgtgk.scoresense.view.ScoreAnalysisView;
import com.ahgtgk.scoresense.view.UserView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/exam-results")
public class ExamResultController {

    private final ExamResultService examResultService;
    private final AnswerService answerService;

    public ExamResultController(ExamResultService examResultService, AnswerService answerService) {
        this.examResultService = examResultService;
        this.answerService = answerService;
    }

    @GetMapping("/report/{examId:\\d+}")
    public ReportView getExamReport(@PathVariable Long examId) {
        return examResultService.getReport(examId);
    }

    @GetMapping("/score-analysis/{examId:\\d+}")
    public List<ScoreAnalysisView> getScoreAnalysis(@PathVariable Long examId) {
        return examResultService.getScoreAnalysis(examId);
    }

    @GetMapping("/answer-analysis/{examId:\\d+}")
    public List<AnswerView> getAnswers(@PathVariable Long examId) {
        return answerService.getAnswers(examId)
                .stream()
                .map(Answer::toView)
                .toList();
    }

    @GetMapping("/{examId:\\d+}/top-15")
    public List<UserView> getTop15Users(@PathVariable Long examId) {
        return examResultService.getTop15Users(examId)
                .stream()
                .map(User::toView)
                .toList();
    }

}
