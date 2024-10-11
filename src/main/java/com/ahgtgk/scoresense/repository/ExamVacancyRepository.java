package com.ahgtgk.scoresense.repository;

import com.ahgtgk.scoresense.entity.ExamVacancy;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExamVacancyRepository extends BaseMapper<ExamVacancy> {

    @Select("""
            select exam_id
              from exam_vacancy
             where vacancy_id = #{vacancyId};
            """)
    List<Long> selectExamsByVacancyId(@Param("vacancyId") Long vacancyId);

}
