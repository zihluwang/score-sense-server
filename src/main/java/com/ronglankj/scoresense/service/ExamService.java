package com.ronglankj.scoresense.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.ronglankj.scoresense.entity.Exam;
import com.ronglankj.scoresense.entity.ExamType;
import com.ronglankj.scoresense.repository.ExamRepository;
import com.ronglankj.scoresense.repository.ExamTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public ExamService(ExamRepository examRepository, ExamTypeRepository examTypeRepository) {
        this.examRepository = examRepository;
        this.examTypeRepository = examTypeRepository;
    }

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
    public List<ExamType> getAllExamTypes() {
        return examTypeRepository.selectAll();
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

}
