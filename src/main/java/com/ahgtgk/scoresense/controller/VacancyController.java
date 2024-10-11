package com.ahgtgk.scoresense.controller;

import com.ahgtgk.scoresense.entity.ExamVacancy;
import com.ahgtgk.scoresense.entity.Vacancy;
import com.ahgtgk.scoresense.exception.BizException;
import com.ahgtgk.scoresense.model.biz.BizVacancy;
import com.ahgtgk.scoresense.model.criteria.SearchVacancyCriteria;
import com.ahgtgk.scoresense.model.request.CreateVacancyRequest;
import com.ahgtgk.scoresense.model.request.UpdateVacancyRequest;
import com.ahgtgk.scoresense.service.ExamVacancyService;
import com.ahgtgk.scoresense.service.VacancyService;
import com.ahgtgk.scoresense.view.VacancyView;
import com.mybatisflex.core.paginate.Page;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/vacancies")
public class VacancyController {

    private final VacancyService vacancyService;
    private final ExamVacancyService examVacancyService;

    public VacancyController(VacancyService vacancyService, ExamVacancyService examVacancyService) {
        this.vacancyService = vacancyService;
        this.examVacancyService = examVacancyService;
    }

    /**
     * 分页获取岗位列表。
     *
     * @param currentPage 页码
     * @param pageSize    页面数据量
     * @param criteria    查询条件
     */
    @GetMapping("/")
    public Page<VacancyView> getVacancies(@RequestParam(defaultValue = "1") Integer currentPage,
                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                          @ModelAttribute SearchVacancyCriteria criteria) {
        var vacancies = vacancyService.getVacancies(currentPage, pageSize, criteria);
        var vacancyIds = vacancies.getRecords().stream()
                .map(Vacancy::getId)
                .toList();

        var examVacancies = examVacancyService.getExamIdsByVacancyIds(vacancyIds);
        return vacancies.map((vacancy) -> vacancy.toBiz(examVacancies.stream()
                        .filter((examVacancy) -> examVacancy.getVacancyId().equals(vacancy.getId()))
                        .map(ExamVacancy::getExamId).toList()))
                .map(BizVacancy::toView);
    }

    @GetMapping("/by-exam/{examId:\\d+}")
    public Page<VacancyView> getVacanciesByExamId(@RequestParam(defaultValue = "1") Integer currentPage,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @PathVariable Long examId) {
        return vacancyService.getVacanciesByExamId(currentPage, pageSize, examId)
                .map(Vacancy::toBiz)
                .map(BizVacancy::toView);
    }

    /**
     * 获取指定岗位信息。
     *
     * @param vacancyId 岗位 ID
     */
    @GetMapping("/{vacancyId:\\d+}")
    public VacancyView getVacancy(@PathVariable Long vacancyId) {
        return vacancyService.getVacancy(vacancyId).toView();
    }

    /**
     * 创建岗位。
     *
     * @param request 创建岗位请求
     */
    @PostMapping("/")
    public VacancyView createVacancy(@Valid @RequestBody CreateVacancyRequest request) {
        return vacancyService.createVacancy(request).toBiz().toView();
    }

    /**
     * 忽略 null 值更新岗位。
     *
     * @param request 更新岗位请求
     */
    @PatchMapping("/")
    public ResponseEntity<Void> updateVacancy(@RequestBody UpdateVacancyRequest request) {
        vacancyService.updateVacancy(request);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除岗位及其绑定的考试关系。
     *
     * @param vacancyId 岗位 ID
     */
    @DeleteMapping("/{vacancyId:\\d+}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Long vacancyId) {
        vacancyService.deleteVacancy(vacancyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importVacancies(@RequestParam MultipartFile attachment) {
        try (var workbook = new XSSFWorkbook(attachment.getInputStream())) {
            vacancyService.importVacancies(workbook);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, "无法打开文件");
        }
    }

}
