package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.config.ConcurrentConfig;
import com.ahgtgk.scoresense.entity.Option;
import com.ahgtgk.scoresense.entity.Question;
import com.ahgtgk.scoresense.entity.Solution;
import com.ahgtgk.scoresense.enumeration.AnswerType;
import com.ahgtgk.scoresense.model.biz.BizQuestion;
import com.ahgtgk.scoresense.model.request.CreateQuestionRequest;
import com.ahgtgk.scoresense.model.request.UpdateQuestionRequest;
import com.ahgtgk.scoresense.repository.OptionRepository;
import com.ahgtgk.scoresense.repository.QuestionRepository;
import com.ahgtgk.scoresense.repository.SolutionRepository;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final SolutionRepository solutionRepository;

    public QuestionService(QuestionRepository questionRepository, OptionRepository optionRepository, SolutionRepository solutionRepository) {
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.solutionRepository = solutionRepository;
    }

    public Question createQuestion(Question question) {
        questionRepository.insert(question);
        return question;
    }

    @Transactional
    public BizQuestion createQuestion(CreateQuestionRequest request) {
        var question = Question.builder()
                .examId(request.examId())
                .id(request.id())
                .questionText(request.questionText())
                .answerType(request.answerType())
                .maxScore(request.maxScore())
                .type(request.type())
                .imageId(request.imageId())
                .build();
        questionRepository.insert(question);

        var bizQuestion = question.toBiz();

        if (List.of(AnswerType.SINGLE_CHOICE, AnswerType.MULTIPLE_CHOICE).contains(request.answerType()) && !request.options().isEmpty()) {
            var options = request.options()
                    .stream()
                    .map((createOptionRequest) -> Option.builder()
                            .examId(request.examId())
                            .questionId(request.id())
                            .id(createOptionRequest.id())
                            .correct(createOptionRequest.correct())
                            .optionText(createOptionRequest.optionText())
                            .build()
                    )
                    .toList();

            optionRepository.insertOptions(options);
            bizQuestion.setOptions(options.stream().map(Option::toBiz).toList());
        }

        return bizQuestion;
    }

    @Transactional
    public void createQuestions(Long examId, List<BizQuestion> bizQuestions) {
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

        var saveSolutionsTask = CompletableFuture.runAsync(() -> {
            var solutions = bizQuestions.stream().map((question) -> Solution.builder()
                            .examId(examId)
                            .questionId(question.getId())
                            .solutionText(question.getSolution())
                            .build())
                    .toList();
            solutionRepository.insertBatch(solutions);
        }, ConcurrentConfig.CACHED_EXECUTORS);

        CompletableFuture.allOf(
                saveOptionTask,
                saveQuestionTask,
                saveSolutionsTask
        ).join();
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

    /**
     * 删除试题及其选项。
     *
     * @param examId     考试 ID
     * @param questionId 试题 ID
     */
    public void deleteQuestion(Long examId, Long questionId) {
        // 删除选项
        var deleteOptionTask = CompletableFuture.runAsync(
                () -> optionRepository.deleteByCondition(Option.OPTION.EXAM_ID.eq(examId)
                        .and(Option.OPTION.QUESTION_ID.eq(questionId))),
                ConcurrentConfig.CACHED_EXECUTORS
        );

        // 删除题目
        var deleteQuestionTask = CompletableFuture.runAsync(
                () -> questionRepository.deleteByCondition(Question.QUESTION.EXAM_ID.eq(examId)
                        .and(Question.QUESTION.ID.eq(questionId))),
                ConcurrentConfig.CACHED_EXECUTORS
        );

        // 删除题解
        var deleteSolutionTask = CompletableFuture.runAsync(
                () -> solutionRepository.deleteSolution(examId, questionId),
                ConcurrentConfig.CACHED_EXECUTORS
        );

        CompletableFuture.allOf(deleteOptionTask, deleteQuestionTask, deleteSolutionTask).join();
    }

    /**
     * 更新试题及其选项。
     */
    @Transactional
    public void updateQuestion(UpdateQuestionRequest request) {
        var questionBuilder = Question.builder()
                .examId(request.examId())
                .id(request.id());

        Optional.ofNullable(request.imageId())
                .ifPresent(questionBuilder::imageId);

        Optional.ofNullable(request.answerType())
                .ifPresent(questionBuilder::answerType);

        Optional.ofNullable(request.maxScore())
                .ifPresent(questionBuilder::maxScore);

        Optional.ofNullable(request.type())
                .ifPresent(questionBuilder::type);

        Optional.ofNullable(request.questionText())
                .filter((questionText) -> !questionText.isBlank())
                .ifPresent(questionBuilder::questionText);

        var question = questionBuilder.build();
        questionRepository.update(question);

        if (Objects.nonNull(request.options()) && !request.options().isEmpty()) {
            var options = request.options()
                    .stream()
                    .map((optionRequest) -> Option.builder()
                            .id(optionRequest.id())
                            .optionText(optionRequest.optionText())
                            .correct(optionRequest.correct())
                            .questionId(request.id())
                            .examId(request.examId())
                            .build())
                    .toList();

            CompletableFuture.runAsync(
                    () -> options.forEach(optionRepository::update),
                    ConcurrentConfig.CACHED_EXECUTORS
            ).join();
        }

        if (Objects.nonNull(request.solution()) && !request.solution().isBlank()) {
            var _solution = Solution.builder()
                    .examId(request.examId())
                    .questionId(request.id())
                    .solutionText(request.solution())
                    .build();

            solutionRepository.update(_solution);
        }
    }

    /**
     * 根据考试 ID 及题目 ID 查询题目解析。
     *
     * @param examId     考试 ID
     * @param questionId 题目 ID
     */
    public String getSolution(Long examId, Long questionId) {
        return solutionRepository.selectSolutionTextById(examId, questionId);
    }
}
