package com.ronglankj.scoresense.repository;

import com.mybatisflex.core.BaseMapper;
import com.ronglankj.scoresense.entity.Exam;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExamRepository extends BaseMapper<Exam> {
}
