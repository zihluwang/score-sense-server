package com.ahgtgk.scoresense.repository;

import com.mybatisflex.core.BaseMapper;
import com.ahgtgk.scoresense.entity.Exam;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExamRepository extends BaseMapper<Exam> {
}
