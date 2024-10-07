package com.ahgtgk.scoresense.repository;

import com.ahgtgk.scoresense.entity.ExamResult;
import com.ahgtgk.scoresense.view.ScoreAnalysisView;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
    Integer selectLowerCount(@Param("examId") Long examId, @Param("userId") Long userId);

    @Select("""
            WITH user_vacancy AS (
                SELECT vacancy_id
                FROM public.exam_result
                WHERE exam_id = #{examId}
                  AND user_id = #{userId}
            )
            SELECT
                CASE
                    WHEN score BETWEEN 0 AND 5 THEN '0-5'
                    WHEN score BETWEEN 6 AND 10 THEN '6-10'
                    WHEN score BETWEEN 11 AND 15 THEN '11-15'
                    WHEN score BETWEEN 16 AND 20 THEN '16-20'
                    WHEN score BETWEEN 21 AND 25 THEN '21-25'
                    WHEN score BETWEEN 26 AND 30 THEN '26-30'
                    WHEN score BETWEEN 31 AND 35 THEN '31-35'
                    WHEN score BETWEEN 36 AND 40 THEN '36-40'
                    WHEN score BETWEEN 41 AND 45 THEN '41-45'
                    WHEN score BETWEEN 46 AND 50 THEN '46-50'
                    WHEN score BETWEEN 51 AND 55 THEN '51-55'
                    WHEN score BETWEEN 56 AND 60 THEN '56-60'
                    WHEN score BETWEEN 61 AND 65 THEN '61-65'
                    WHEN score BETWEEN 66 AND 70 THEN '66-70'
                    WHEN score BETWEEN 71 AND 75 THEN '71-75'
                    WHEN score BETWEEN 76 AND 80 THEN '76-80'
                    WHEN score BETWEEN 81 AND 85 THEN '81-85'
                    WHEN score BETWEEN 86 AND 90 THEN '86-90'
                    WHEN score BETWEEN 91 AND 95 THEN '91-95'
                    WHEN score BETWEEN 96 AND 100 THEN '96-100'
                END AS score_range,
                COUNT(*) AS attendee_count
            FROM public.exam_result
            WHERE exam_id = #{examId}
              AND vacancy_id = (SELECT vacancy_id FROM user_vacancy)
            GROUP BY score_range
            ORDER BY score_range;
            """)
    List<ScoreAnalysisView> selectScoreAnalysis(@Param("examId") Long examId, @Param("userId") Long userId);
}
