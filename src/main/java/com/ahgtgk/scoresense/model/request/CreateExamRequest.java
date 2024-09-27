package com.ahgtgk.scoresense.model.request;

import org.springframework.web.multipart.MultipartFile;

/**
 * 创建考试的请求。
 *
 * @param attachment 考试试题文件
 * @param name       考试的名称
 * @param province   举办考试的省份代码
 * @param prefecture 举办考试的城市代码
 */
public record CreateExamRequest(
        MultipartFile attachment,
        String name,
        String province,
        String prefecture
) {
}
