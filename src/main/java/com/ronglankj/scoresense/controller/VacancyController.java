package com.ronglankj.scoresense.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.ronglankj.scoresense.entity.ExamVacancy;
import com.ronglankj.scoresense.entity.Vacancy;
import com.ronglankj.scoresense.service.VacancyService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/vacancies")
public class VacancyController {

    private final VacancyService vacancyService;

    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @GetMapping("/")
    public Page<Vacancy> getPaginatedVacancies(@RequestParam(required = false, defaultValue = "1") Integer currentPage,
                                               @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                               @RequestParam(required = false) Long examId,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) String province,
                                               @RequestParam(required = false) String prefecture) {
        // 表结构
        final var EXAM_VACANCY = ExamVacancy.EXAM_VACANCY;
        final var VACANCY = Vacancy.VACANCY;

        // 创建查询条件
        var queryWrapper = QueryWrapper.create();
        Optional.ofNullable(examId)
                .ifPresent((_examId) -> queryWrapper.leftJoin(EXAM_VACANCY).on(EXAM_VACANCY.EXAM_ID.eq(_examId)));
        Optional.ofNullable(name)
                .ifPresent((_name) -> queryWrapper.and(VACANCY.NAME.like(_name)));
        Optional.ofNullable(province)
                .ifPresent((_province) -> queryWrapper.and(VACANCY.PROVINCE.eq(_province)));
        Optional.ofNullable(prefecture)
                .ifPresent((_prefecture) -> queryWrapper.and(VACANCY.PREFECTURE.eq(_prefecture)));
        return vacancyService.getPaginatedVacancies(currentPage, pageSize, queryWrapper);
    }

    @GetMapping("/{vacancyId}")
    public Vacancy getVacancy(@PathVariable Long vacancyId) {
        return vacancyService.getVacancy(vacancyId);
    }

}
