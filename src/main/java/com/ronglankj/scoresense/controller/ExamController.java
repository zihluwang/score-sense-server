package com.ronglankj.scoresense.controller;

import com.mybatisflex.core.paginate.Page;
import com.ronglankj.scoresense.entity.Exam;
import com.ronglankj.scoresense.entity.ExamType;
import com.ronglankj.scoresense.model.request.CreateExamRequest;
import com.ronglankj.scoresense.model.request.CreateExamTypeRequest;
import com.ronglankj.scoresense.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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



    /**
     * 根据考试类型查询考试列表。
     *
     * @param currentPage  当前页码
     * @param pageSize     页面大小
     * @param examTypeId   考试类型 ID
     * @param divisionCode 行政区划编码
     * @return 考试信息分页数据
     */
    @GetMapping("/types/{examTypeId}")
    public Page<Exam> getExamByType(@RequestParam(defaultValue = "1") Integer currentPage,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @PathVariable Integer examTypeId,
                                    @RequestParam(required = false) String divisionCode) {
        return examService.getExamsByExamType(examTypeId, divisionCode, currentPage, pageSize);
    }

    /**
     * 获取所有考试类型。
     *
     * @param currentPage 当前页码
     * @param pageSize    页面大小
     * @return 分页考试类型数据
     */
    @GetMapping("/types")
    public Page<ExamType> getAllExamTypes(@RequestParam(defaultValue = "1") Integer currentPage,
                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                          @RequestParam(required = false) String name) {
        return examService.getExamTypes(currentPage, pageSize, name);
    }

    @PostMapping("/types")
    public ExamType createExamType(@RequestBody CreateExamTypeRequest request) {
        return examService.createExamType(request.name());
    }

}
