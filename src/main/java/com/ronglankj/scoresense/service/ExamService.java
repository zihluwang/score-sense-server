package com.ronglankj.scoresense.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.ronglankj.scoresense.entity.Exam;
import com.ronglankj.scoresense.repository.ExamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 考试业务。
 *
 * @author zihluwang
 */
@Slf4j
@Service
public class ExamService {

    private final ExamRepository examRepository;

    @Autowired
    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
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

}
