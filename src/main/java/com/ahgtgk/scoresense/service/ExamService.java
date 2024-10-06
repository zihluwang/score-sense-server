package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.entity.Exam;
import com.ahgtgk.scoresense.entity.ExamType;
import com.ahgtgk.scoresense.enumeration.AnswerType;
import com.ahgtgk.scoresense.enumeration.Status;
import com.ahgtgk.scoresense.exception.DataConflictException;
import com.ahgtgk.scoresense.model.biz.BizOption;
import com.ahgtgk.scoresense.model.biz.BizQuestion;
import com.ahgtgk.scoresense.model.criteria.SearchExamCriteria;
import com.ahgtgk.scoresense.model.request.CreateExamRequest;
import com.ahgtgk.scoresense.model.request.CreateExamTypeRequest;
import com.ahgtgk.scoresense.model.request.UpdateExamRequest;
import com.ahgtgk.scoresense.repository.ExamRepository;
import com.ahgtgk.scoresense.repository.ExamTypeRepository;
import com.ahgtgk.scoresense.view.ExamTypeView;
import com.ahgtgk.scoresense.view.ExamView;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.onixbyte.guid.GuidCreator;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final GuidCreator<Long> examIdCreator;
    private final ExamVacancyService examVacancyService;
    private final SequenceService sequenceService;
    private final ExamTypeRepository examTypeRepository;

    @Autowired
    public ExamService(ExamRepository examRepository, GuidCreator<Long> examIdCreator, ExamVacancyService examVacancyService, SequenceService sequenceService, ExamTypeRepository examTypeRepository) {
        this.examRepository = examRepository;
        this.examIdCreator = examIdCreator;
        this.examVacancyService = examVacancyService;
        this.sequenceService = sequenceService;
        this.examTypeRepository = examTypeRepository;
    }

    private final static String SEQ_KEY = "exam_type";

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

    /**
     * 解析包含题目的 Excel 文件。
     *
     * @param workbook Excel 工作簿
     * @return 解析出来的题目
     */
    public List<BizQuestion> resolveQuestions(Workbook workbook) {
        // 获取默认 Sheet
        var sheet = workbook.getSheetAt(0);

        var questionId = 1L;

        var questions = new ArrayList<BizQuestion>();
        // 遍历每一行
        for (var row : sheet) {
            // 跳过前两行
            if (row.getRowNum() < 2) {
                continue;
            }

            var questionBuilder = BizQuestion.builder();
            questionBuilder.id(questionId++);

            // 解析题目题干
            questionBuilder.questionText(row.getCell(0).getStringCellValue());

            // 解析正确选项
            var correctOptions = Arrays.stream(row.getCell(5).getStringCellValue().split(",")).toList();

            // 解析选项 A
            var optionAlpha = BizOption.builder()
                    .id("A")
                    .optionText(row.getCell(1).getStringCellValue())
                    .correct(correctOptions.contains("A"))
                    .build();
            // 解析选项 B
            var optionBravo = BizOption.builder()
                    .id("B")
                    .optionText(row.getCell(2).getStringCellValue())
                    .correct(correctOptions.contains("B"))
                    .build();
            // 解析选项 C
            var optionCharlie = BizOption.builder()
                    .id("C")
                    .optionText(row.getCell(3).getStringCellValue())
                    .correct(correctOptions.contains("C"))
                    .build();
            // 解析选项 D
            var optionDelta = BizOption.builder()
                    .id("D")
                    .optionText(row.getCell(4).getStringCellValue())
                    .correct(correctOptions.contains("D"))
                    .build();
            // 添加选项
            questionBuilder.options(List.of(optionAlpha, optionBravo, optionCharlie, optionDelta));

            // 设置答题类型
            questionBuilder.answerType(correctOptions.size() == 1 ?
                    AnswerType.SINGLE_CHOICE : AnswerType.MULTIPLE_CHOICE);

            // 设置题型
            questionBuilder.type(Double.valueOf(row.getCell(6).getNumericCellValue()).intValue());

            // 设置题目满分
            questionBuilder.maxScore(Double.valueOf(row.getCell(7).getNumericCellValue() * 100).intValue());

            questions.add(questionBuilder.build());
        }

        return questions;
    }

    /**
     * 创建考试类型。
     *
     * @param request 创建考试类型请求
     */
    public ExamType createExamType(CreateExamTypeRequest request) {
        // 判断考试类型是否可以创建
        var canCreate = examTypeRepository.selectCountByCondition(ExamType.EXAM_TYPE.NAME.eq(request.name())) == 0;
        if (!canCreate) {
            throw new DataConflictException("考试类型名称");
        }

        // 构建考试类型
        var examType = ExamType.builder()
                .id(sequenceService.next(SEQ_KEY).intValue())
                .name(request.name())
                .build();

        examTypeRepository.insert(examType);
        return examType;
    }

    /**
     * 分页获取考试类型数据。
     *
     * @param currentPage 当前页码
     * @param pageSize    页面大小
     * @param name        考试名称
     */
    public Page<ExamType> getExamTypes(Integer currentPage, Integer pageSize, String name) {
        var queryWrapper = QueryWrapper.create();
        if (Objects.nonNull(name) && !name.isBlank()) {
            queryWrapper.and(ExamType.EXAM_TYPE.NAME.like(name));
        }
        queryWrapper.orderBy(ExamType.EXAM_TYPE.ID, true);

        return examTypeRepository.paginate(currentPage, pageSize, queryWrapper);
    }

    /**
     * 根据考试类型分页查询考试信息。
     *
     * @param currentPage 当前页面
     * @param pageSize 页面大小
     * @param examTypeId 考试类型 ID
     */
    public Page<Exam> getExamsByExamType(Integer currentPage, Integer pageSize, Long examTypeId) {
        return examRepository.paginate(currentPage, pageSize, QueryWrapper.create()
                .where(Exam.EXAM.TYPE.eq(examTypeId))
                .orderBy(Exam.EXAM.ID, false));
    }
}
