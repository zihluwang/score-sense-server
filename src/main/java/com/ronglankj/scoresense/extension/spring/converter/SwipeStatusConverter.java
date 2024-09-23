package com.ronglankj.scoresense.extension.spring.converter;

import com.ronglankj.scoresense.enumeration.SwipeStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

public class SwipeStatusConverter implements Converter<String, SwipeStatus> {

    @Override
    public SwipeStatus convert(@NonNull String source) {
        var code = Integer.parseInt(source);
        return SwipeStatus.byCode(code);
    }
}
