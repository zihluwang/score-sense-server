package com.ronglankj.scoresense.enumeration;

import com.mybatisflex.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum SwipeStatus {

    ENABLED(1),
    DISABLED(0);

    @EnumValue
    private final Integer code;

    SwipeStatus(Integer code) {
        this.code = code;
    }

    public static SwipeStatus byCode(Integer code) {
        return Arrays.stream(values())
                .filter((item) -> code.equals(item.code))
                .findFirst()
                .orElse(null);
    }

}
