package com.ahgtgk.scoresense.extension.spring.converter;

import com.ahgtgk.scoresense.enumeration.SwipeStatus;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class SwipeStatusConverter implements Converter<String, SwipeStatus> {

    @Override
    public SwipeStatus convert(@NonNull String source) {
        var code = Integer.parseInt(source);
        return SwipeStatus.byCode(code);
    }
}
