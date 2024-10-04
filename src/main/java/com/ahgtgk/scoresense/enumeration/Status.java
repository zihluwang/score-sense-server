package com.ahgtgk.scoresense.enumeration;

import com.mybatisflex.annotation.EnumValue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@Getter
public enum Status {

    DISABLED(0),
    ENABLED(1),
    ;

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
