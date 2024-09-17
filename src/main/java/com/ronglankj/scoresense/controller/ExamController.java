package com.ronglankj.scoresense.controller;

import com.mybatisflex.core.paginate.Page;
import com.ronglankj.scoresense.entity.Exam;
import com.ronglankj.scoresense.model.request.CreateExamRequest;
import com.ronglankj.scoresense.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exams")
public class ExamController {

    private final ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/")
    public Page<Exam> getExams(@RequestParam(required = false, defaultValue = "1") Integer currentPage,
                               @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return examService.getExamPage(currentPage, pageSize);
    }

    @PostMapping("/")
    public ResponseEntity<Void> createExam(@ModelAttribute CreateExamRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

}
