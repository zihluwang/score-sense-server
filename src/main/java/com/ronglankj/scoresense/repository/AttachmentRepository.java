package com.ronglankj.scoresense.repository;

import com.mybatisflex.core.BaseMapper;
import com.ronglankj.scoresense.entity.Attachment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttachmentRepository extends BaseMapper<Attachment> {
}
