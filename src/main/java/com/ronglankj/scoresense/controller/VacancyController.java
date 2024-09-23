package com.ronglankj.scoresense.controller;

import com.mybatisflex.core.paginate.Page;
import com.ronglankj.scoresense.entity.Vacancy;
import com.ronglankj.scoresense.service.VacancyService;
import org.springframework.web.bind.annotation.*;

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
                                               @RequestParam(required = false) String divisionCode) {
        return vacancyService.getPaginatedVacancies(currentPage, pageSize, examId, name, divisionCode);
    }

    @GetMapping("/{vacancyId}")
    public Vacancy getVacancy(@PathVariable Long vacancyId) {
        return vacancyService.getVacancy(vacancyId);
    }

}
