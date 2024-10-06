package com.ahgtgk.scoresense.controller;

import com.ahgtgk.scoresense.model.request.CreateQuestionRequest;
import com.ahgtgk.scoresense.model.request.UpdateQuestionRequest;
import com.ahgtgk.scoresense.service.QuestionService;
import com.ahgtgk.scoresense.view.QuestionView;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/")
    public QuestionView createQuestion(@Valid @RequestBody CreateQuestionRequest request) {
        return questionService.createQuestion(request).toView();
    }

    @PatchMapping("/")
    public ResponseEntity<Void> updateQuestion(@Valid @RequestBody UpdateQuestionRequest request) {
        questionService.updateQuestion(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{examId:\\d+}/{questionId:\\d+}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long examId, @PathVariable Long questionId) {
        questionService.deleteQuestion(examId, questionId);
        return ResponseEntity.noContent().build();
    }

}
