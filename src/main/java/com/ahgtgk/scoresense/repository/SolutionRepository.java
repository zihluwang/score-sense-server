package com.ahgtgk.scoresense.repository;

import com.ahgtgk.scoresense.entity.Solution;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SolutionRepository extends BaseMapper<Solution> {

    @Select("""
            select solution_text
              from solution
             where exam_id = #{examId}
               and question_id = #{questionId};
            """)
    String selectSolutionTextById(@Param("examId") Long examId, @Param("questionId") Long questionId);

    @Delete("""
            delete from solution
             where exam_id = #{examId} 
               and question_id = #{questionId};
            """)
    int deleteSolution(@Param("examId") Long examId, @Param("questionId") Long questionId);

}
