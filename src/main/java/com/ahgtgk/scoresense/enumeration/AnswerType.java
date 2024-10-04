package com.ahgtgk.scoresense.enumeration;

import com.mybatisflex.annotation.EnumValue;

import java.util.Arrays;
import java.util.Objects;

public enum AnswerType {

    SINGLE_CHOICE(0),
    MULTIPLE_CHOICE(1),
    SUBJECTIVE(2);

    private final int code;

    AnswerType(int code) {
        this.code = code;
    }

    @EnumValue
    public int getCode() {
        return code;
    }

    public static AnswerType byCode(Integer code) {
        return Arrays.stream(values())
                .filter((value) -> Objects.equals(code, value.code))
                .findFirst()
                .orElse(null);
    }

}
