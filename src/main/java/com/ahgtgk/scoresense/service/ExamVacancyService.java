package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.entity.ExamVacancy;
import com.ahgtgk.scoresense.repository.ExamVacancyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ExamVacancyService {

    private final ExamVacancyRepository examVacancyRepository;

    public ExamVacancyService(ExamVacancyRepository examVacancyRepository) {
        this.examVacancyRepository = examVacancyRepository;
    }

    /**
     * 将岗位与考试进行绑定。
     */
    @Transactional
    public void bindExamsToVacancy(Long vacancyId, List<Long> examIds) {
        // 删除与该 vacancyId 相关的所有绑定关系
        deleteByVacancyId(vacancyId);

        // 插入新的绑定关系
        var examVacancies = examIds.stream()
                .map((examId) -> ExamVacancy.builder()
                        .examId(examId)
                        .vacancyId(vacancyId)
                        .build())
                .toList();

        examVacancyRepository.insertBatch(examVacancies);
    }

    /**
     * 删除与指定 vacancyId 相关的所有关联
     *
     * @param vacancyId 岗位 ID
     */
    public void deleteByVacancyId(Long vacancyId) {
        examVacancyRepository.deleteByCondition(ExamVacancy.EXAM_VACANCY.VACANCY_ID.eq(vacancyId));
    }

    /**
     * 删除与指定 examId 相关的所有关联
     *
     * @param examId 岗位 ID
     */
    public void deleteByExamId(Long examId) {
        examVacancyRepository.deleteByCondition(ExamVacancy.EXAM_VACANCY.EXAM_ID.eq(examId));
    }

    /**
     * 根据岗位 ID 获取绑定的考试信息。
     *
     * @param vacancyId 岗位 ID
     */
    public List<Long> getExamsByVacancyId(Long vacancyId) {
        return examVacancyRepository.selectExamsByVacancyId(vacancyId);
    }

}
