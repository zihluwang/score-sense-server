package com.ahgtgk.scoresense.repository;

import com.mybatisflex.core.BaseMapper;
import com.ahgtgk.scoresense.entity.Option;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OptionRepository extends BaseMapper<Option> {

    @Insert("""
            <script>
            insert into option(exam_id, question_id, id, option_text, correct) values
            <foreach collection="options" item="option" separator=",">
                (#{option.examId}, #{option.questionId}, #{option.id}, #{option.optionText}, #{option.correct})
            </foreach>;
            </script>
            """)
    int insertOptions(@Param("options") List<Option> options);

}
