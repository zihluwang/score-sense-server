package com.ronglankj.scoresense.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.ronglankj.scoresense.entity.Vacancy;
import com.ronglankj.scoresense.repository.VacancyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
     * @param queryWrapper 查询条件
     * @return 查询到的岗位分页信息
     */
    public Page<Vacancy> getPaginatedVacancies(Integer currentPage, Integer pageSize, QueryWrapper queryWrapper) {
        // 设置排序方式
        queryWrapper = queryWrapper.orderBy(Vacancy.VACANCY.ID, false);
        return vacancyRepository.paginate(currentPage, pageSize, queryWrapper);
    }

    public Vacancy getVacancy(Long id) {
        return vacancyRepository.selectOneByCondition(Vacancy.VACANCY.ID.eq(id));
    }

}
