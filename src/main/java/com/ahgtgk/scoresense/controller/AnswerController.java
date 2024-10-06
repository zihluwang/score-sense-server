package com.ahgtgk.scoresense.controller;

import com.ahgtgk.scoresense.entity.Answer;
import com.ahgtgk.scoresense.model.request.AnswerQuestionRequest;
import com.ahgtgk.scoresense.service.AnswerService;
import com.ahgtgk.scoresense.view.AnswerView;
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

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
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

}
