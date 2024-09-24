package com.ronglankj.scoresense.controller;

import com.mybatisflex.core.paginate.Page;
import com.ronglankj.scoresense.entity.Vacancy;
import com.ronglankj.scoresense.model.request.CreateVacancyRequest;
import com.ronglankj.scoresense.model.request.UpdateVacancyRequest;
import com.ronglankj.scoresense.model.response.ActionResponse;
import com.ronglankj.scoresense.service.VacancyService;
import com.ronglankj.scoresense.util.DateTimeUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/vacancies")
public class VacancyController {

    private final VacancyService vacancyService;

    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    /**
     * 查询岗位列表。
     *
     * @param currentPage  当前页码，默认为 (@code 1)
     * @param pageSize     当前页面大小，默认为 {@code 10}
     * @param examId       考试 ID，额外查询条件，精准匹配
     * @param name         岗位名称，额外查询条件，模糊匹配
     * @param divisionCode 行政区划编码，额外查询条件，仅接受2字符长度（省份）或4字符长度（地市），精准匹配
     * @return 岗位列表分页数据
     */
    @GetMapping("/")
    public Page<Vacancy> getPaginatedVacancies(@RequestParam(required = false, defaultValue = "1") Integer currentPage,
                                               @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                               @RequestParam(required = false) Long examId,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) String divisionCode) {
        return vacancyService.getPaginatedVacancies(currentPage, pageSize, examId, name, divisionCode);
    }

    /**
     * 查询岗位信息。
     *
     * @param vacancyId 岗位 ID
     * @return 岗位信息
     */
    @GetMapping("/{vacancyId}")
    public Vacancy getVacancy(@PathVariable Long vacancyId) {
        return vacancyService.getVacancy(vacancyId);
    }

    @PostMapping("/")
    public Vacancy createVacancy(@RequestBody CreateVacancyRequest request) {
        return vacancyService.createVacancy(Vacancy.builder()
                .name(request.name())
                .province(request.province())
                .prefecture(request.prefecture()), request.examIds());
    }

    @PatchMapping("/")
    public Vacancy updateVacancy(@RequestBody UpdateVacancyRequest request) {
        var vacancy = Vacancy.builder()
                .id(request.id())
                .name(request.name())
                .province(request.province())
                .prefecture(request.prefecture())
                .build();
        return vacancyService.updateVacancy(vacancy, request.examIds());
    }

    @DeleteMapping("/{vacancyId}")
    public ActionResponse deleteVacancy(@PathVariable Long vacancyId) {
        vacancyService.deleteVacancyById(vacancyId);
        return ActionResponse.builder()
                .timestamp(DateTimeUtils.toInstant(LocalDateTime.now()))
                .message("岗位删除成功")
                .build();
    }
}
