package com.ahgtgk.scoresense.repository;

import com.ahgtgk.scoresense.entity.ExamResult;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ExamResultRepository extends BaseMapper<ExamResult> {

    @Select("""
            WITH user_vacancy AS (
                SELECT vacancy_id
                FROM public.exam_result
                WHERE exam_id = #{examId}
                  AND user_id = #{userId}
            )
            SELECT rank
            FROM (
                SELECT user_id,
                       score,
                       RANK() OVER (ORDER BY score DESC) AS rank
                FROM public.exam_result
                WHERE exam_id = #{examId}
                  AND vacancy_id = (SELECT vacancy_id FROM user_vacancy)
            ) AS ranking
            WHERE user_id = #{userId};
            
            """)
    Integer selectRank(@Param("examId") Long examId, @Param("userId") Long userId);

    @Select("""
            WITH user_vacancy AS (
                SELECT vacancy_id
                FROM public.exam_result
                WHERE exam_id = #{examId}
                  AND user_id = #{userId}
            )
            SELECT COUNT(*) AS user_count
            FROM public.exam_result
            WHERE exam_id = #{examId}
              AND vacancy_id = (SELECT vacancy_id FROM user_vacancy);
            """)
    Integer selectAttendeeCount(@Param("examId") Long examId, @Param("userId") Long userId);

    @Select("""
            WITH user_vacancy AS (
                SELECT vacancy_id
                FROM public.exam_result
                WHERE exam_id = #{examId}
                  AND user_id = #{userId}
            )
            SELECT SUM(score) AS total_score_sum
            FROM public.exam_result
            WHERE exam_id = #{examId}
              AND vacancy_id = (SELECT vacancy_id FROM user_vacancy);
            """)
    Integer selectTotalScore(@Param("examId") Long examId, @Param("userId") Long userId);

    @Select("""
            WITH user_info AS (
                SELECT vacancy_id, score
                FROM public.exam_result
                WHERE exam_id = #{examId}
                  AND user_id = #{userId}
            )
            SELECT COUNT(*) AS lower_score_count
            FROM public.exam_result
            WHERE exam_id = #{examId}
              AND vacancy_id = (SELECT vacancy_id FROM user_info)
              AND score < (SELECT score FROM user_info);
            """)
    Integer selectLowerCount(Long examId, Long userId);
}
