package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.config.ConcurrentConfig;
import com.ahgtgk.scoresense.entity.Option;
import com.ahgtgk.scoresense.entity.Question;
import com.ahgtgk.scoresense.model.biz.BizQuestion;
import com.ahgtgk.scoresense.repository.OptionRepository;
import com.ahgtgk.scoresense.repository.QuestionRepository;
import com.ahgtgk.scoresense.view.QuestionView;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    public QuestionService(QuestionRepository questionRepository, OptionRepository optionRepository) {
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
    }

    public Question createQuestion(Question question) {
        questionRepository.insert(question);
        return question;
    }

    @Transactional
    public int createQuestions(Long examId, List<BizQuestion> bizQuestions) {
        var saveOptionTask = CompletableFuture.runAsync(() -> {
            var options = bizQuestions.stream()
                    .flatMap((question) -> question.getPersistentOptions(examId, question.getId()).stream())
                    .toList();

            optionRepository.insertOptions(options);
        }, ConcurrentConfig.CACHED_EXECUTORS);

        var saveQuestionTask = CompletableFuture.supplyAsync(() -> {
            var questions = bizQuestions.stream()
                    .map((question) -> question.toPersistent(examId))
                    .toList();

            return questionRepository.insertQuestions(questions);
        }, ConcurrentConfig.CACHED_EXECUTORS);

        CompletableFuture.allOf(saveOptionTask, saveQuestionTask).join();
        return saveQuestionTask.join();
    }

    public List<BizQuestion> getQuestions(Long examId) {
        var questions = questionRepository.selectListByQuery(QueryWrapper.create()
                .where(Question.QUESTION.EXAM_ID.eq(examId))
                .orderBy(Question.QUESTION.ID, true));
        var options = optionRepository.selectListByQuery(QueryWrapper.create()
                .where(Option.OPTION.EXAM_ID.eq(examId))
                .orderBy(Option.OPTION.QUESTION_ID, true));

        return questions.stream()
                .map((question) -> {
                    var _options = options.stream()
                            .filter((option) -> option.getQuestionId().equals(question.getId()))
                            .map(Option::toBiz)
                            .toList();
                    return question.toBiz(_options);
                })
                .toList();
    }

}
