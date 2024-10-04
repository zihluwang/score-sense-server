package com.ahgtgk.scoresense.model.biz;

import com.ahgtgk.scoresense.entity.Option;
import com.ahgtgk.scoresense.view.OptionView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizOption {

    private String id;

    private String optionText;

    private Boolean correct;

    public OptionView toView() {
        return OptionView.builder()
                .id(id)
                .optionText(optionText)
                .correct(correct)
                .build();
    }

    public Option toPersistent(Long examId, Long questionId) {
        return Option.builder()
                .examId(examId)
                .questionId(questionId)
                .id(id)
                .optionText(optionText)
                .correct(correct)
                .build();
    }
}
