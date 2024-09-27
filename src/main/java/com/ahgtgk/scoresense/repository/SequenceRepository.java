package com.ahgtgk.scoresense.repository;

import com.mybatisflex.core.BaseMapper;
import com.ahgtgk.scoresense.entity.Sequence;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SequenceRepository extends BaseMapper<Sequence> {
}
