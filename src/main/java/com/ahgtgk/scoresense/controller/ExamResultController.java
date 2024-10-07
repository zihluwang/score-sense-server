package com.ahgtgk.scoresense.controller;

import com.ahgtgk.scoresense.service.ExamResultService;
import com.ahgtgk.scoresense.view.ReportView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/exam-results")
public class ExamResultController {

    private final ExamResultService examResultService;

    public ExamResultController(ExamResultService examResultService) {
        this.examResultService = examResultService;
    }

    @GetMapping("/report/{examId:\\d+}")
    public ReportView getExamReport(@PathVariable Long examId) {
        return examResultService.getReport(examId);
    }



}
