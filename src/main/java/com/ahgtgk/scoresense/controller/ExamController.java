package com.ahgtgk.scoresense.controller;


import com.ahgtgk.scoresense.entity.Exam;
import com.ahgtgk.scoresense.model.criteria.SearchExamCriteria;
import com.ahgtgk.scoresense.model.request.CreateExamRequest;
import com.ahgtgk.scoresense.model.request.UpdateExamRequest;
import com.ahgtgk.scoresense.service.ExamService;
import com.ahgtgk.scoresense.view.ExamView;
import com.mybatisflex.core.paginate.Page;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/")
    public Page<ExamView> getExams(@RequestParam(defaultValue = "1") Integer currentPage,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @ModelAttribute SearchExamCriteria criteria) {
        return examService.getExams(currentPage, pageSize, criteria).map(Exam::toView);
    }

    @GetMapping("/{examId:\\d+}")
    public ExamView getExam(@PathVariable Long examId) {
        return examService.getExam(examId).toView();
    }

    @PostMapping("/")
    public ExamView createExam(@Valid @RequestBody CreateExamRequest request) {
        return examService.createExam(request).toView();
    }

    @PatchMapping("/")
    public ResponseEntity<Void> updateExam(@Valid @RequestBody UpdateExamRequest request) {
        examService.updateExam(request);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{examId:\\d+}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long examId) {
        examService.deleteExam(examId);
        return ResponseEntity.noContent().build();
    }

}
