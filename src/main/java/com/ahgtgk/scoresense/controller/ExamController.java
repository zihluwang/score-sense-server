package com.ahgtgk.scoresense.controller;

import com.ahgtgk.scoresense.config.ConcurrentConfig;
import com.ahgtgk.scoresense.entity.Exam;
import com.ahgtgk.scoresense.entity.ExamType;
import com.ahgtgk.scoresense.exception.BizException;
import com.ahgtgk.scoresense.model.biz.BizClientExam;
import com.ahgtgk.scoresense.model.biz.BizQuestion;
import com.ahgtgk.scoresense.model.criteria.SearchExamCriteria;
import com.ahgtgk.scoresense.model.request.*;
import com.ahgtgk.scoresense.service.ExamResultService;
import com.ahgtgk.scoresense.service.ExamService;
import com.ahgtgk.scoresense.service.QuestionService;
import com.ahgtgk.scoresense.view.*;
import com.mybatisflex.core.paginate.Page;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/exams")
public class ExamController {

    private final ExamService examService;
    private final QuestionService questionService;
    private final ExamResultService examResultService;

    public ExamController(ExamService examService,
                          QuestionService questionService, ExamResultService examResultService) {
        this.examService = examService;
        this.questionService = questionService;
        this.examResultService = examResultService;
    }

    @GetMapping("/")
    public Page<ExamView> getExams(@RequestParam(defaultValue = "1") Integer currentPage,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @ModelAttribute SearchExamCriteria criteria) {
        return examService.getExams(currentPage, pageSize, criteria).map(Exam::toView);
    }

    @GetMapping("/{examId:\\d+}")
    public FullExamView getExam(@PathVariable Long examId) {
        var exam = examService.getExam(examId);

        var questions = questionService.getQuestions(examId);

        return FullExamView.builder()
                .id(String.valueOf(exam.getId()))
                .name(exam.getName())
                .description(exam.getDescription())
                .province(exam.getProvince())
                .prefecture(exam.getPrefecture())
                .status(exam.getStatus().getCode())
                .type(exam.getType())
                .questions(questions.stream().map(BizQuestion::toView).toList())
                .build();
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

    @PostMapping("/import")
    public FullExamView importExam(@ModelAttribute ImportExamRequest request) {
        // 检测考试是否存在，不存在则直接创建
        var exam = Optional.ofNullable(examService.getExam(request.id()))
                .orElseGet(() -> examService.createExam(CreateExamRequest.builder()
                        .name(request.name())
                        .type(request.type())
                        .province(request.province())
                        .prefecture(request.prefecture())
                        .status(request.status())
                        .build()));

        try (var workbook = new XSSFWorkbook(request.attachment().getInputStream())) { // 提取文件
            // 解析试题
            var questions = examService.resolveQuestions(workbook);

            // 存储试题
            questionService.createQuestions(exam.getId(), questions);

            return FullExamView.builder()
                    .id(String.valueOf(exam.getId()))
                    .name(exam.getName())
                    .description(exam.getDescription())
                    .province(exam.getProvince())
                    .prefecture(exam.getPrefecture())
                    .status(exam.getStatus().getCode())
                    .type(exam.getType())
                    .questions(questions.stream().map(BizQuestion::toView).toList())
                    .build();
        } catch (IOException e) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, "无法打开附件");
        }
    }

    @GetMapping("/participate/{examId:\\d+}")
    public FullExamView participateExam(@PathVariable Long examId) {
        // 获取考试信息
        var exam = examService.getExam(examId);

        var preParticipateExamTask = CompletableFuture.runAsync(
                () -> examService.participateExam(examId),
                ConcurrentConfig.CACHED_EXECUTORS
        );

        // 获取考试题目
        var questions = questionService.getQuestions(examId)
                .stream()
                .peek((question) -> {
                    var options = question.getOptions().stream()
                            .peek((option) -> option.setCorrect(null))
                            .toList();
                    question.setOptions(options);
                })
                .toList();

        // 等待预备考试结果
        preParticipateExamTask.join();

        return FullExamView.builder()
                .id(String.valueOf(exam.getId()))
                .name(exam.getName())
                .type(exam.getType())
                .description(exam.getDescription())
                .province(exam.getProvince())
                .prefecture(exam.getPrefecture())
                .status(exam.getStatus().getCode())
                .questions(questions.stream().map(BizQuestion::toView).toList())
                .build();
    }

    @PostMapping("/types")
    public ExamTypeView createExamType(@RequestBody CreateExamTypeRequest request) {
        return examService.createExamType(request).toView();
    }

    @GetMapping("/types")
    public Page<ExamTypeView> getExamTypes(@RequestParam(defaultValue = "1") Integer currentPage,
                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                           @RequestParam(required = false) String name) {
        return examService.getExamTypes(currentPage, pageSize, name)
                .map(ExamType::toView);
    }

    @GetMapping("/types/{examTypeId:\\d+}")
    public Page<ClientExamView> getExamsByExamType(@RequestParam(defaultValue = "1") Integer currentPage,
                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                   @PathVariable Long examTypeId,
                                                   @RequestParam String divisionCode) {
        return examService.getExamsByExamType(currentPage, pageSize, examTypeId, divisionCode)
                .map(BizClientExam::toView);
    }

    @PatchMapping("/types")
    public ExamTypeView updateExamType(@Valid @RequestBody UpdateExamTypeRequest request) {
        return examService.updateExamType(request)
                .toView();
    }

    @DeleteMapping("/types/{examTypeId:\\d+}")
    public ResponseEntity<Void> deleteExamType(@PathVariable Integer examTypeId) {
        examService.deleteExamType(examTypeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/finish/{examId:\\d+}/{vacancyId:\\d+}")
    public ExamResultView finishExam(@PathVariable Long examId, @PathVariable Long vacancyId) {
        return examResultService.finishExam(examId, vacancyId).toView();
    }

    @GetMapping("/{examId:\\d+}/solutions")
    public List<QuestionView> getSolutions(@PathVariable Long examId) {
        return questionService.getQuestionsWithSolution(examId)
                .stream()
                .map(BizQuestion::toView)
                .toList();
    }

    @GetMapping("/historical")
    public List<ExamView> getHistoricalExams() {
        return examService.getHistoricalExams()
                .stream()
                .map(Exam::toView)
                .toList();
    }

}
