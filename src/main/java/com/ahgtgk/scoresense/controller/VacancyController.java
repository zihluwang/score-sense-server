package com.ahgtgk.scoresense.controller;

import com.ahgtgk.scoresense.entity.Vacancy;
import com.ahgtgk.scoresense.model.criteria.SearchVacancyCriteria;
import com.ahgtgk.scoresense.model.request.CreateVacancyRequest;
import com.ahgtgk.scoresense.model.request.UpdateVacancyRequest;
import com.ahgtgk.scoresense.service.VacancyService;
import com.ahgtgk.scoresense.view.VacancyView;
import com.mybatisflex.core.paginate.Page;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/vacancies")
public class VacancyController {

    private final VacancyService vacancyService;

    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
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
        return vacancyService.getVacancies(currentPage, pageSize, criteria)
                .map(Vacancy::toView);
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
        return vacancyService.createVacancy(request).toView();
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

}