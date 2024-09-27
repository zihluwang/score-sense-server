package com.ahgtgk.scoresense.repository;

import com.mybatisflex.core.BaseMapper;
import com.ahgtgk.scoresense.entity.Question;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionRepository extends BaseMapper<Question> {
}
