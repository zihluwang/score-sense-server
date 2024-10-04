package com.ahgtgk.scoresense.repository;

import com.mybatisflex.core.BaseMapper;
import com.ahgtgk.scoresense.entity.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionRepository extends BaseMapper<Question> {

    @Insert("""
            <script>
            insert into question(exam_id, id, type, answer_type, question_text, image_id, max_score) values
            <foreach collection="questions" item="question" separator=",">
                (#{question.examId}, #{question.id}, #{question.type}, #{question.answerType}, #{question.questionText},
                 #{question.imageId}, #{question.maxScore})
            </foreach>;
            </script>
            """)
    int insertQuestions(@Param("questions") List<Question> questions);

}
