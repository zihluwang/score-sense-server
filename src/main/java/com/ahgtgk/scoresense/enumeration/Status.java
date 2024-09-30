package com.ahgtgk.scoresense.enumeration;

import com.mybatisflex.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Status {

    ENABLED(1),
    DISABLED(0);

    @EnumValue
    private final Integer code;

    Status(Integer code) {
        this.code = code;
    }

    public static Status byCode(Integer code) {
        return Arrays.stream(values())
                .filter((item) -> code.equals(item.code))
                .findFirst()
                .orElse(null);
    }

}
