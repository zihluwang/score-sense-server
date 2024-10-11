package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.entity.Exam;
import com.ahgtgk.scoresense.entity.ExamResult;
import com.ahgtgk.scoresense.entity.ExamType;
import com.ahgtgk.scoresense.enumeration.AnswerType;
import com.ahgtgk.scoresense.enumeration.Status;
import com.ahgtgk.scoresense.exception.BizException;
import com.ahgtgk.scoresense.exception.DataConflictException;
import com.ahgtgk.scoresense.model.biz.BizOption;
import com.ahgtgk.scoresense.model.biz.BizQuestion;
import com.ahgtgk.scoresense.model.criteria.SearchExamCriteria;
import com.ahgtgk.scoresense.model.request.CreateExamRequest;
import com.ahgtgk.scoresense.model.request.CreateExamTypeRequest;
import com.ahgtgk.scoresense.model.request.UpdateExamRequest;
import com.ahgtgk.scoresense.model.request.UpdateExamTypeRequest;
import com.ahgtgk.scoresense.repository.ExamRepository;
import com.ahgtgk.scoresense.repository.ExamResultRepository;
import com.ahgtgk.scoresense.repository.ExamTypeRepository;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import com.onixbyte.guid.GuidCreator;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private final UserService userService;
    private final ExamResultRepository examResultRepository;

    @Autowired
    public ExamService(ExamRepository examRepository,
                       GuidCreator<Long> examIdCreator,
                       ExamVacancyService examVacancyService,
                       SequenceService sequenceService,
                       ExamTypeRepository examTypeRepository,
                       UserService userService,
                       ExamResultRepository examResultRepository) {
        this.examRepository = examRepository;
        this.examIdCreator = examIdCreator;
        this.examVacancyService = examVacancyService;
        this.sequenceService = sequenceService;
        this.examTypeRepository = examTypeRepository;
        this.userService = userService;
        this.examResultRepository = examResultRepository;
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

        if (Objects.nonNull(criteria.status())) {
            queryWrapper.and(Exam.EXAM.STATUS.eq(criteria.status()));
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

            // 设置解析
            var _solution = Optional.ofNullable(row.getCell(8))
                    .map((cell) -> switch (cell.getCellType()) {
                        case _NONE, BLANK, BOOLEAN, ERROR, FORMULA -> "略";
                        case NUMERIC -> String.valueOf(cell.getNumericCellValue());
                        case STRING -> cell.getStringCellValue();
                    })
                    .orElse("略");
            questionBuilder.solution(_solution);

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
     * @param pageSize    页面大小
     * @param examTypeId  考试类型 ID
     */
    public Page<Exam> getExamsByExamType(Integer currentPage, Integer pageSize, Long examTypeId) {
        var queryWrapper = QueryWrapper.create();
        if (examTypeId != 0) {
            queryWrapper.where(Exam.EXAM.TYPE.eq(examTypeId));
        }
        queryWrapper.and(Exam.EXAM.STATUS.eq(Status.ENABLED));
        queryWrapper.orderBy(Exam.EXAM.ID, false);
        return examRepository.paginate(currentPage, pageSize, queryWrapper);
    }

    /**
     * 更新考试类型。
     *
     * @param request 更新考试类型请求
     */
    public ExamType updateExamType(UpdateExamTypeRequest request) {
        var examType = UpdateEntity.of(ExamType.class, request.id());
        if (Objects.nonNull(request.name()) && !request.name().isBlank()) {
            examType.setName(request.name());
        }

        examTypeRepository.update(examType);
        return examType;
    }

    /**
     * 删除考试类型。
     *
     * @param examTypeId 考试类型 ID
     */
    public void deleteExamType(Integer examTypeId) {
        var canDelete = examRepository.selectCountByCondition(Exam.EXAM.TYPE.eq(examTypeId)) == 0;
        if (!canDelete) {
            throw new BizException(HttpStatus.UNPROCESSABLE_ENTITY, "该考试类型仍有绑定的考试");
        }

        examTypeRepository.deleteById(examTypeId);
    }

    @Transactional
    public void participateExam(Long examId) {
        var currentUser = userService.getCurrentUser();

        var examResult = ExamResult.builder()
                .vacancyId(0L) // 暂时先不记录岗位信息
                .userId(currentUser.getId())
                .examId(examId)
                .score(0)
                .build();
        examResultRepository.insert(examResult);
    }

    public List<Exam> getHistoricalExams() {
        var user = userService.getCurrentUser();
        return examRepository.selectListByQuery(QueryWrapper.create()
                .from(Exam.EXAM)
                .join(ExamResult.EXAM_RESULT).on(Exam.EXAM.ID.eq(ExamResult.EXAM_RESULT.EXAM_ID))
                .where(ExamResult.EXAM_RESULT.USER_ID.eq(user.getId()))
                .and(ExamResult.EXAM_RESULT.COMPLETED_AT.isNull())
                .orderBy(Exam.EXAM.ID, false));
    }
}
