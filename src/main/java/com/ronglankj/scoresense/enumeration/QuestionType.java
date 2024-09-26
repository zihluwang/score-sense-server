package com.ronglankj.scoresense.enumeration;

import com.mybatisflex.annotation.EnumValue;
import lombok.Getter;

/**
 * 试题类型
 *
 * @author zihluwang
 */
@Getter
public enum QuestionType {

    VERBAL_REASONING(0, "言语理解与表达"),
    GENERAL_KNOWLEDGE(1, "常识判断"),
    QUANTITATIVE_APTITUDE(2, "数量关系"),
    LOGICAL_REASONING(3, "判断推理"),
    DATA_INTERPRETATION(4, "资料分析"),
    ADMINISTRATIVE_ABILITY(5, "行政职业能力测试"),
    PUBLIC_BASIC_KNOWLEDGE(6, "公共基础知识"),
    ANALYTICAL_WRITING_AND_POLICY_ARGUMENT(7, "申论");

    @EnumValue
    private final Integer value;

    private final String description;

    QuestionType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

}
