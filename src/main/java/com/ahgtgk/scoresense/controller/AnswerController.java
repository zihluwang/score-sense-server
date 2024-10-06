package com.ahgtgk.scoresense.controller;

import com.ahgtgk.scoresense.entity.Answer;
import com.ahgtgk.scoresense.model.request.AnswerQuestionRequest;
import com.ahgtgk.scoresense.service.AnswerService;
import com.ahgtgk.scoresense.service.ExamResultService;
import com.ahgtgk.scoresense.view.AnswerView;
import com.ahgtgk.scoresense.view.ExamResultView;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerService answerService;
    private final ExamResultService examResultService;

    public AnswerController(AnswerService answerService, ExamResultService examResultService) {
        this.answerService = answerService;
        this.examResultService = examResultService;
    }

    @GetMapping("/continue/{examId:\\d+}")
    public List<AnswerView> continueExam(@PathVariable Long examId) {
        return answerService.continueAnswer(examId).stream()
                .map(Answer::toView)
                .toList();
    }

    @PostMapping("/")
    public ResponseEntity<Void> answerQuestion(@Valid @RequestBody AnswerQuestionRequest request) {
        answerService.answerQuestion(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/finish/{examId:\\d+}/{vacancyId:\\d+}")
    public ExamResultView finishExam(@PathVariable Long examId, @PathVariable Long vacancyId) {
        return examResultService.finishExam(examId, vacancyId).toView();
    }

}
