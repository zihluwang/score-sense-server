package com.ronglankj.scoresense.repository;

import com.mybatisflex.core.BaseMapper;
import com.ronglankj.scoresense.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository extends BaseMapper<User> {
}
