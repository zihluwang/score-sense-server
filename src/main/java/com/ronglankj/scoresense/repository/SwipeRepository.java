package com.ronglankj.scoresense.repository;

import com.mybatisflex.core.BaseMapper;
import com.ronglankj.scoresense.entity.Swipe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SwipeRepository extends BaseMapper<Swipe> {

    @Update("""
            update swipe
            set sequence = sequence - 1
            where sequence > #{removedSequence};""")
    int updateSequenceAfter(@Param("removedSequence") Integer removedSequence);

    @Update("""
            update swipe
            set sequence = null;""")
    int removeAllSequences();

}
