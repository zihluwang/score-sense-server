package com.ahgtgk.scoresense.controller;

import com.mybatisflex.core.paginate.Page;
import com.ahgtgk.scoresense.entity.Exam;
import com.ahgtgk.scoresense.entity.ExamType;
import com.ahgtgk.scoresense.exception.BaseBizException;
import com.ahgtgk.scoresense.model.request.CreateExamRequest;
import com.ahgtgk.scoresense.model.request.CreateExamTypeRequest;
import com.ahgtgk.scoresense.model.request.UpdateExamTypeRequest;
import com.ahgtgk.scoresense.model.response.ActionResponse;
import com.ahgtgk.scoresense.service.ExamService;
import com.ahgtgk.scoresense.util.DateTimeUtils;
import com.ahgtgk.scoresense.view.ExamView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/exams")
public class ExamController {

    private final ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    /**
     * 获取分页考试列表。
     *
     * @param currentPage 当前页码
     * @param pageSize    页面大小
     * @return 考试列表分页数据
     */
    @GetMapping("/")
    public Page<ExamView> getExams(@RequestParam(required = false, defaultValue = "1") Integer currentPage,
                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return examService.getExamPage(currentPage, pageSize).map(Exam::toView);
    }

    /**
     * 创建考试。
     *
     * @param request 创建考试请求
     * @return 创建的考试信息
     */
    @PostMapping("/")
    public ExamView createExam(@ModelAttribute CreateExamRequest request) {
        // todo 实现该功能
        throw new BaseBizException(HttpStatus.SERVICE_UNAVAILABLE, "接口暂未开放");
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
    public Page<ExamView> getExamByType(@RequestParam(defaultValue = "1") Integer currentPage,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @PathVariable Integer examTypeId,
                                    @RequestParam(required = false) String divisionCode) {
        return examService.getExamsByExamType(examTypeId, divisionCode, currentPage, pageSize).map(Exam::toView);
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

    /**
     * 创建考试类型。
     *
     * @param request 创建考试类型请求
     * @return 创建的考试类型
     */
    @PostMapping("/types")
    public ExamType createExamType(@RequestBody CreateExamTypeRequest request) {
        return examService.createExamType(request.name());
    }

    /**
     * 修改考试类型。
     *
     * @param request 修改考试类型请求
     * @return 被修改后的考试类型
     */
    @PatchMapping("/types")
    public ExamType updateExamType(@RequestBody UpdateExamTypeRequest request) {
        return examService.updateExamType(request);
    }

    /**
     * 删除考试类型。
     *
     * @param typeId 需要被删除的考试类型 ID
     * @return 操作的响应
     */
    @DeleteMapping("/types/{typeId}")
    public ActionResponse deleteExamType(@PathVariable Integer typeId) {
        examService.deleteExamType(typeId);
        return ActionResponse.builder()
                .message("考试类型删除成功！")
                .timestamp(DateTimeUtils.toInstant(LocalDateTime.now()))
                .build();
    }

}
