package com.ronglankj.scoresense.repository;

import com.mybatisflex.core.BaseMapper;
import com.ronglankj.scoresense.entity.Vacancy;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VacancyRepository extends BaseMapper<Vacancy> {
}
