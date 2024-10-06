package com.ahgtgk.scoresense.model.biz;

import com.ahgtgk.scoresense.entity.Option;
import com.ahgtgk.scoresense.entity.Question;
import com.ahgtgk.scoresense.enumeration.AnswerType;
import com.ahgtgk.scoresense.view.QuestionView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizQuestion {

    private Long id;

    private Integer type;

    private AnswerType answerType;

    private String questionText;

    private Long imageId;

    private Integer maxScore;

    private List<BizOption> options;

    private String solution;

    public QuestionView toView() {
        return QuestionView.builder()
                .id(id)
                .type(type)
                .answerType(answerType)
                .questionText(questionText)
                .imageId(imageId)
                .maxScore(maxScore)
                .options(options.stream().map(BizOption::toView).toList())
                .solution(solution)
                .build();
    }

    public Question toPersistent(Long examId) {
        return Question.builder()
                .examId(examId)
                .id(id)
                .type(type)
                .answerType(answerType)
                .questionText(questionText)
                .imageId(imageId)
                .maxScore(maxScore)
                .build();
    }

    public List<Option> getPersistentOptions(Long examId, Long questionId) {
        return options.stream()
                .map((option) -> option.toPersistent(examId, questionId))
                .toList();
    }

}
