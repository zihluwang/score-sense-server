package com.ronglankj.scoresense.repository;

import com.mybatisflex.core.BaseMapper;
import com.ronglankj.scoresense.entity.Answer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnswerRepository extends BaseMapper<Answer> {
}
