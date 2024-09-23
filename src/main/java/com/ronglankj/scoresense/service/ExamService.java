package com.ronglankj.scoresense.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.onixbyte.guid.GuidCreator;
import com.ronglankj.scoresense.entity.Exam;
import com.ronglankj.scoresense.entity.ExamType;
import com.ronglankj.scoresense.repository.ExamRepository;
import com.ronglankj.scoresense.repository.ExamTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    private final GuidCreator<Long> examTypeIdCreator;
    private final SequenceService sequenceService;

    @Autowired
    public ExamService(ExamRepository examRepository,
                       ExamTypeRepository examTypeRepository,
                       @Qualifier("examTypeIdCreator") GuidCreator<Long> examTypeIdCreator,
                       SequenceService sequenceService) {
        this.examRepository = examRepository;
        this.examTypeRepository = examTypeRepository;
        this.examTypeIdCreator = examTypeIdCreator;
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
    public Page<Exam> getExamPage(Integer currentPage, Integer pageSize) {
        return examRepository.paginate(currentPage, pageSize, QueryWrapper.create()
                .orderBy(Exam.EXAM.ID, false));
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

    public ExamType createExamType(String name) {
        var canCreate = examTypeRepository.selectCountByCondition(ExamType.EXAM_TYPE.NAME.eq(name)) == 0;
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

}
