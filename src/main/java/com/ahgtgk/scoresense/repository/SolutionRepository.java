package com.ahgtgk.scoresense.repository;

import com.ahgtgk.scoresense.entity.Solution;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SolutionRepository extends BaseMapper<Solution> {

    @Select("""
            select solution_text
              from solution
             where exam_id = #{examId}
               and question_id = #{questionId};
            """)
    String selectSolutionTextById(Long examId, Long questionId);

}
