package com.ahgtgk.scoresense.repository;

import com.mybatisflex.core.BaseMapper;
import com.ahgtgk.scoresense.entity.Vacancy;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VacancyRepository extends BaseMapper<Vacancy> {
}
