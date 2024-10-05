package com.ahgtgk.scoresense.repository;

import com.mybatisflex.core.BaseMapper;
import com.ahgtgk.scoresense.entity.Sequence;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SequenceRepository extends BaseMapper<Sequence> {

    @Select("""
            select next
            from sequence
            where key = #{key}
            for update;
            """)
    Long selectNextByKey(@Param("key") String key);

    @Update("""
            UPDATE public.sequence\s
            SET next = next + 1\s
            WHERE key = #{key}
            """)
    int updateNextByKey(@Param("key") String key);

    @Insert("""
            INSERT INTO public.sequence (key, next)
            VALUES (#{key}, #{next});
            """)
    int insertNext(@Param("key") String key, @Param("next") Long next);
}
