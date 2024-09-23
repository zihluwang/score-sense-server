package com.ronglankj.scoresense.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.ronglankj.scoresense.entity.ExamVacancy;
import com.ronglankj.scoresense.entity.Vacancy;
import com.ronglankj.scoresense.repository.VacancyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;

    public VacancyService(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
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

}
