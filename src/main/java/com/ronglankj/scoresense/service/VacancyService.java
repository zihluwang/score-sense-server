package com.ronglankj.scoresense.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.onixbyte.guid.GuidCreator;
import com.ronglankj.scoresense.config.ConcurrentConfig;
import com.ronglankj.scoresense.entity.ExamVacancy;
import com.ronglankj.scoresense.entity.Vacancy;
import com.ronglankj.scoresense.exception.BaseBizException;
import com.ronglankj.scoresense.repository.ExamVacancyRepository;
import com.ronglankj.scoresense.repository.VacancyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final GuidCreator<Long> vacancyIdCreator;
    private final ExamService examService;
    private final ExamVacancyRepository examVacancyRepository;

    public VacancyService(VacancyRepository vacancyRepository,
                          GuidCreator<Long> vacancyIdCreator, ExamService examService, ExamVacancyRepository examVacancyRepository) {
        this.vacancyRepository = vacancyRepository;
        this.vacancyIdCreator = vacancyIdCreator;
        this.examService = examService;
        this.examVacancyRepository = examVacancyRepository;
    }

    /**
     * 分页查询岗位信息。
     *
     * @param currentPage  当前页码
     * @param pageSize     页面数据量大小
     * @param examId       考试 ID
     * @param name         考试名称
     * @param divisionCode 行政区划代码，省份2位或地市4位
     * @return 查询到的岗位分页信息
     */
    public Page<Vacancy> getPaginatedVacancies(Integer currentPage,
                                               Integer pageSize,
                                               Long examId,
                                               String name,
                                               String divisionCode) {
        // 设置排序方式
        var queryWrapper = QueryWrapper.create();

        Optional.ofNullable(examId)
                .ifPresent((_examId) -> queryWrapper.leftJoin(ExamVacancy.EXAM_VACANCY)
                        .on(ExamVacancy.EXAM_VACANCY.EXAM_ID.eq(_examId)));
        Optional.ofNullable(name)
                .ifPresent((_name) -> queryWrapper.and(Vacancy.VACANCY.NAME.like(_name)));
        Optional.ofNullable(divisionCode)
                .ifPresent((_divisionCode) -> {
                    if (_divisionCode.isBlank()) {
                        return;
                    }

                    if (_divisionCode.length() == 2) {
                        queryWrapper.and(Vacancy.VACANCY.PROVINCE.eq(_divisionCode));
                    } else if (_divisionCode.length() == 4) {
                        queryWrapper.and(Vacancy.VACANCY.PREFECTURE.eq(_divisionCode));
                    }
                });

        return vacancyRepository.paginate(currentPage, pageSize, queryWrapper.orderBy(Vacancy.VACANCY.ID, false));
    }

    public Vacancy getVacancy(Long id) {
        return vacancyRepository.selectOneByCondition(Vacancy.VACANCY.ID.eq(id));
    }

    public Vacancy createVacancy(Vacancy.VacancyBuilder builder, List<Long> examIds) {
        var vacancyId = vacancyIdCreator.nextId();
        var examCount = examService.countExamsByExamIds(examIds);
        if (examCount != examIds.size()) {
            throw new BaseBizException(HttpStatus.BAD_REQUEST, "考试 ID 不存在");
        }
        var vacancy = builder.id(vacancyId).build();
        var examVacancies = examIds.stream()
                .map((examId) -> ExamVacancy.builder()
                        .examId(examId)
                        .vacancyId(vacancyId)
                        .build())
                .toList();
        var saveVacancyTask = CompletableFuture.runAsync(
                () -> vacancyRepository.insert(vacancy), ConcurrentConfig.CACHED_EXECUTORS);
        var saveExamVacanciesTask = CompletableFuture.runAsync(
                () -> examVacancyRepository.insertBatch(examVacancies), ConcurrentConfig.CACHED_EXECUTORS);
        CompletableFuture.allOf(saveVacancyTask, saveExamVacanciesTask).join();
        return vacancy;
    }

    public Vacancy updateVacancy(Vacancy vacancy, List<Long> examIds) {
        var examCount = examService.countExamsByExamIds(examIds);
        if (examCount != examIds.size()) {
            throw new BaseBizException(HttpStatus.BAD_REQUEST, "考试 ID 不存在");
        }

        var canUpdate = vacancyRepository.selectOneByEntityId(vacancy) != null;
        if (!canUpdate) {
            throw new BaseBizException(HttpStatus.BAD_REQUEST, "岗位不存在");
        }

        var examVacancies = examIds.stream()
                .map((examId) -> ExamVacancy.builder()
                        .examId(examId)
                        .vacancyId(vacancy.getId())
                        .build())
                .toList();

        var updateVacancyTask = CompletableFuture.runAsync(() -> vacancyRepository.update(vacancy, true), ConcurrentConfig.CACHED_EXECUTORS);
        var updateExamVacancyTask = CompletableFuture.runAsync(() -> {
            examVacancyRepository.deleteByCondition(ExamVacancy.EXAM_VACANCY.VACANCY_ID.eq(vacancy.getId()));
            examVacancyRepository.insertBatch(examVacancies);
        }, ConcurrentConfig.CACHED_EXECUTORS);
        CompletableFuture.allOf(updateVacancyTask, updateExamVacancyTask).join();
        return vacancy;
    }

    public void deleteVacancyById(Long vacancyId) {
        var deleteVacancyTask = CompletableFuture.runAsync(
                () -> vacancyRepository.deleteByCondition(Vacancy.VACANCY.ID.eq(vacancyId)),
                ConcurrentConfig.CACHED_EXECUTORS);
        var deleteExamVacancyTask = CompletableFuture.runAsync(
                () -> examVacancyRepository.deleteByCondition(ExamVacancy.EXAM_VACANCY.VACANCY_ID.eq(vacancyId)),
                ConcurrentConfig.CACHED_EXECUTORS);
        CompletableFuture.allOf(deleteVacancyTask, deleteExamVacancyTask).join();
    }
}
