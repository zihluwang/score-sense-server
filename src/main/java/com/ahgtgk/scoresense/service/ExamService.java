package com.ahgtgk.scoresense.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.ahgtgk.scoresense.entity.Exam;
import com.ahgtgk.scoresense.entity.ExamType;
import com.ahgtgk.scoresense.exception.BaseBizException;
import com.ahgtgk.scoresense.model.request.UpdateExamTypeRequest;
import com.ahgtgk.scoresense.repository.ExamRepository;
import com.ahgtgk.scoresense.repository.ExamTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 考试业务。
 *
 * @author zihluwang
 */
@Slf4j
@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamTypeRepository examTypeRepository;
    private final SequenceService sequenceService;

    @Autowired
    public ExamService(ExamRepository examRepository,
                       ExamTypeRepository examTypeRepository,
                       SequenceService sequenceService) {
        this.examRepository = examRepository;
        this.examTypeRepository = examTypeRepository;
        this.sequenceService = sequenceService;
    }

    private final String sequenceKey = "EXAM_TYPE";

    /**
     * 分页查询考试数据。
     *
     * @param currentPage 当前考试页码
     * @param pageSize    页面大小
     * @return 考试分页数据
     */
    public Page<Exam> getExamPage(Integer currentPage, Integer pageSize, String divisionCode, String name) {
        var queryWrapper = QueryWrapper.create();

        Optional.ofNullable(divisionCode)
                .filter((_divisionCode) -> !_divisionCode.isBlank())
                .ifPresent((_divisionCode) -> {
                    if (_divisionCode.length() == 2) {
                        queryWrapper.and(Exam.EXAM.PROVINCE.eq(_divisionCode));
                    } else if (_divisionCode.length() == 4) {
                        queryWrapper.and(Exam.EXAM.PREFECTURE.eq(_divisionCode));
                    }
                });
        Optional.ofNullable(name)
                .filter((_name) -> !_name.isBlank())
                .ifPresent((_name) -> queryWrapper.and(Exam.EXAM.NAME.like(_name)));

        return examRepository.paginate(currentPage, pageSize, queryWrapper.orderBy(Exam.EXAM.ID, false));
    }

    /**
     * 获取所有考试类型。
     *
     * @return 所有考试类型
     */
    public Page<ExamType> getExamTypes(Integer currentPage, Integer pageSize, String name) {
        var queryWrapper = QueryWrapper.create();
        if (Objects.nonNull(name) && !name.isBlank()) {
            queryWrapper.and(ExamType.EXAM_TYPE.NAME.like(name));
        }
        return examTypeRepository.paginate(currentPage, pageSize, queryWrapper);
    }

    /**
     * 根据考试类型分页查询考试列表。
     *
     * @return 根据考试类型分类的考试列表分页数据
     */
    public Page<Exam> getExamsByExamType(Integer examType, String divisionCode, Integer currentPage, Integer pageSize) {
        var queryCondition = Exam.EXAM.TYPE.eq(examType);
        if (Objects.nonNull(divisionCode) && !divisionCode.isBlank()) {
            if (divisionCode.length() == 2) {
                queryCondition.and(Exam.EXAM.PROVINCE.eq(divisionCode));
            } else if (divisionCode.length() == 4) {
                queryCondition.and(Exam.EXAM.PREFECTURE.eq(divisionCode));
            }
        }
        return examRepository.paginate(currentPage, pageSize, queryCondition);
    }

    public boolean isExamTypeNameDuplicated(String name) {
        return examTypeRepository.selectCountByCondition(ExamType.EXAM_TYPE.NAME.eq(name)) == 0;
    }

    /**
     * 创建考试类型。
     *
     * @param name 考试类型名称
     * @return 被创建的考试类型
     */
    public ExamType createExamType(String name) {
        var canCreate = isExamTypeNameDuplicated(name);
        if (!canCreate) {
            return examTypeRepository.selectOneByCondition(ExamType.EXAM_TYPE.NAME.eq(name));
        }
        var examType = ExamType.builder()
                .id(sequenceService.next(sequenceKey).intValue())
                .name(name)
                .build();
        examTypeRepository.insert(examType);
        return examType;
    }

    /**
     * 更新考试类型。
     *
     * @param request 修改请求
     * @return 修改后的考试类型
     */
    public ExamType updateExamType(UpdateExamTypeRequest request) {
        var examType = examTypeRepository.selectOneByCondition(ExamType.EXAM_TYPE.ID.eq(request.id()));
        var canUpdate = isExamTypeNameDuplicated(request.name());
        if (!canUpdate) {
            throw new BaseBizException(HttpStatus.BAD_REQUEST, "考试类型名称重复，无法执行更新。");
        } else {
            examType.setName(request.name());
        }
        examTypeRepository.update(examType);
        return examType;
    }

    /**
     * 删除考试类型。
     *
     * @param typeId 考试类型 ID
     */
    public void deleteExamType(Integer typeId) {
        // 检查是否有考试类型存在
        var isExamTypeExisted = examTypeRepository.selectCountByCondition(ExamType.EXAM_TYPE.ID.eq(typeId)) >= 1;
        if (!isExamTypeExisted) {
            throw new BaseBizException(HttpStatus.NO_CONTENT, "无指定考试类型，未执行删除操作");
        }

        // 检查是否还有未修改的考试
        var examCount = countExamsByType(typeId);

        // 若考试类型还关联有考试，则无法执行删除
        if (examCount != 0) {
            throw new BaseBizException(HttpStatus.CONFLICT, "指定考试类型还关联有考试，无法删除。");
        }

        // 执行删除
        examTypeRepository.deleteById(typeId);
    }

    /**
     * 根据考试类型统计考试数量。
     *
     * @param typeId 考试类型 ID
     * @return 考试类型下的考试数量
     */
    public long countExamsByType(Integer typeId) {
        return examRepository.selectCountByCondition(Exam.EXAM.TYPE.eq(typeId));
    }

    /**
     * 根据考试 ID 统计考试数量。
     *
     * @param examIds 考试 ID 列表
     * @return 考试的数量
     */
    public long countExamsByExamIds(List<Long> examIds) {
        return examRepository.selectCountByCondition(Exam.EXAM.ID.in(examIds));
    }
}
