package com.ronglankj.scoresense.repository;

import com.mybatisflex.core.BaseMapper;
import com.ronglankj.scoresense.entity.Question;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionRepository extends BaseMapper<Question> {
}
