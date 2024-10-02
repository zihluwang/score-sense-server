package com.ahgtgk.scoresense.extension.spring.core.convert.converter;

import com.ahgtgk.scoresense.enumeration.Status;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class StatusConverter implements Converter<String, Status> {

    @Override
    public Status convert(@NonNull String source) {
        var code = Integer.parseInt(source);
        return Status.byCode(code);
    }
}
