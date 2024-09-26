package com.ronglankj.scoresense.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProvinceResponse(
        String code,
        String name,
        List<PrefectureResponse> prefectures
) {
}
