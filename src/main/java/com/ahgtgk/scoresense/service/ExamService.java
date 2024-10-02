package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.entity.Exam;
import com.ahgtgk.scoresense.enumeration.Status;
import com.ahgtgk.scoresense.exception.DataConflictException;
import com.ahgtgk.scoresense.model.criteria.SearchExamCriteria;
import com.ahgtgk.scoresense.model.request.CreateExamRequest;
import com.ahgtgk.scoresense.model.request.UpdateExamRequest;
import com.ahgtgk.scoresense.repository.ExamRepository;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.onixbyte.guid.GuidCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final GuidCreator<Long> examIdCreator;
    private final ExamVacancyService examVacancyService;

    @Autowired
    public ExamService(ExamRepository examRepository, GuidCreator<Long> examIdCreator, ExamVacancyService examVacancyService) {
        this.examRepository = examRepository;
        this.examIdCreator = examIdCreator;
        this.examVacancyService = examVacancyService;
    }

    /**
     * 分页查找考试。
     *
     * @param currentPage 当前页码
     * @param pageSize    页面大小
     * @param criteria    查询条件
     */
    public Page<Exam> getExams(Integer currentPage, Integer pageSize, SearchExamCriteria criteria) {
        var queryWrapper = QueryWrapper.create();

        // 拼接查询条件
        if (Objects.nonNull(criteria.name()) && !criteria.name().isBlank()) {
            queryWrapper.and(Exam.EXAM.NAME.like(criteria.name()));
        }

        if (Objects.nonNull(criteria.divisionCode()) && !criteria.divisionCode().isBlank()) {
            if (criteria.divisionCode().length() == 2) {
                queryWrapper.and(Exam.EXAM.PROVINCE.eq(criteria.divisionCode()));
            } else if (criteria.divisionCode().length() == 4) {
                queryWrapper.and(Exam.EXAM.PREFECTURE.eq(criteria.divisionCode()));
            }
        }

        queryWrapper.orderBy(Exam.EXAM.ID, false);

        return examRepository.paginate(currentPage, pageSize, queryWrapper);
    }

    /**
     * 根据考试 ID 查找考试详细信息。
     *
     * @param examId 考试 ID
     */
    public Exam getExam(Long examId) {
        return examRepository.selectOneById(examId);
    }

    /**
     * 创建考试。
     *
     * @param request 创建考试请求
     */
    public Exam createExam(CreateExamRequest request) {
        // 检测数据冲突
        var canCreate = examRepository.selectCountByCondition(Exam.EXAM.NAME.eq(request.name())) == 0;
        if (!canCreate) {
            throw new DataConflictException("考试名称");
        }

        // 构建考试信息
        var exam = Exam.builder()
                .id(examIdCreator.nextId())
                .name(request.name())
                .type(request.type())
                .description(request.description())
                .status(Status.ENABLED)
                .province(request.province())
                .prefecture(request.prefecture())
                .build();

        // 执行创建
        examRepository.insert(exam);

        return exam;
    }

    /**
     * 忽略 null 值更新考试。
     *
     * @param request 更新考试请求
     */
    public void updateExam(UpdateExamRequest request) {
        var examBuilder = Exam.builder()
                .id(request.id());

        // 构建被更新考试实体
        Optional.ofNullable(request.name())
                .filter((_name) -> !_name.isBlank())
                .ifPresent(examBuilder::name);

        Optional.ofNullable(request.description())
                .filter((_desc) -> !_desc.isBlank())
                .ifPresent(examBuilder::description);

        Optional.ofNullable(request.province())
                .filter((_province) -> !_province.isBlank())
                .ifPresent(examBuilder::province);

        Optional.ofNullable(request.prefecture())
                .filter((_prefecture) -> !_prefecture.isBlank())
                .ifPresent(examBuilder::prefecture);

        Optional.ofNullable(request.type())
                .ifPresent(examBuilder::type);

        Optional.ofNullable(request.status())
                .ifPresent(examBuilder::status);

        examRepository.update(examBuilder.build(), true);
    }

    /**
     * 删除考试。
     *
     * @param examId 考试 ID
     */
    @Transactional
    public void deleteExam(Long examId) {
        examVacancyService.deleteByExamId(examId);
        examRepository.deleteById(examId);
    }
}
