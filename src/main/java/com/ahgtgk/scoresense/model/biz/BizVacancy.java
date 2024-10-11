package com.ahgtgk.scoresense.model.biz;

import com.ahgtgk.scoresense.view.VacancyView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizVacancy {

    private Long id;
    private String name;
    private String province;
    private String prefecture;
    private List<String> examIds;

    public VacancyView toView() {
        return VacancyView.builder()
                .id(String.valueOf(id))
                .name(name)
                .province(province)
                .prefecture(prefecture)
                .examIds(examIds.stream().map(String::valueOf).toList())
                .build();
    }

}

