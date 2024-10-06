package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.entity.Answer;
import com.ahgtgk.scoresense.enumeration.AnswerType;
import com.ahgtgk.scoresense.exception.BizException;
import com.ahgtgk.scoresense.model.biz.BizOption;
import com.ahgtgk.scoresense.model.request.AnswerQuestionRequest;
import com.ahgtgk.scoresense.repository.AnswerRepository;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionService questionService;
    private final UserService userService;

    public AnswerService(AnswerRepository answerRepository, QuestionService questionService, UserService userService) {
        this.answerRepository = answerRepository;
        this.questionService = questionService;
        this.userService = userService;
    }

    /**
     * 继续回答，获取该考试下的所有答题记录。
     *
     * @param examId 考试 ID
     */
    public List<Answer> continueAnswer(Long examId) {
        var currentUser = userService.getCurrentUser();
        return answerRepository.selectListByCondition(Answer.ANSWER.EXAM_ID.eq(examId)
                .and(Answer.ANSWER.USER_ID.eq(currentUser.getId())));
    }

    /**
     * 回答问题。
     *
     * @param request 用户的答案
     */
    public void answerQuestion(AnswerQuestionRequest request) {
        var currentUser = userService.getCurrentUser();

        var question = questionService.getQuestion(request.examId(), request.questionId());
        if (AnswerType.SUBJECTIVE == question.getAnswerType()) {
            throw new BizException(HttpStatus.BAD_REQUEST, "本平台暂不支持回答主观题！");
        }

        var score = switch (question.getAnswerType()) {
            case SINGLE_CHOICE -> {
                // 获取正确选项
                var correctOption = question.getOptions()
                        .stream()
                        .filter(BizOption::getCorrect)
                        .findFirst()
                        .orElseThrow(() -> new BizException(HttpStatus.INTERNAL_SERVER_ERROR, "该题目没有正确答案，请联系客服人员"));

                if (request.answerText().getFirst().equalsIgnoreCase(correctOption.getId())) {
                    yield question.getMaxScore();
                } else {
                    yield 0;
                }
            }
            case MULTIPLE_CHOICE -> {
                // 获取正确选项
                var correctOptions = question.getOptions()
                        .stream()
                        .filter(BizOption::getCorrect)
                        .map(BizOption::getId)
                        .toList();

                if (areListsEqual(correctOptions, request.answerText())) {
                    yield question.getMaxScore();
                } else {
                    yield 0;
                }
            }
            default -> throw new BizException(HttpStatus.BAD_REQUEST, "本平台暂不支持回答主观题！");
        };

        var answer = Answer.builder()
                .examId(request.examId())
                .questionId(request.questionId())
                .userId(currentUser.getId())
                .answerText(String.join(",", request.answerText()))
                .score(score)
                .submittedAt(LocalDateTime.now())
                .build();

        answerRepository.insertOrUpdate(answer);
    }

    /**
     * 获取用户指定考试的答题信息。
     *
     * @param examId 考试 ID
     */
    public List<Answer> getAnswers(Long examId) {
        var userId = userService.getCurrentUser().getId();
        return answerRepository.selectListByQuery(QueryWrapper.create()
                .where(Answer.ANSWER.EXAM_ID.eq(examId))
                .and(Answer.ANSWER.USER_ID.eq(userId)));
    }

    public void fillUnansweredQuestions(Long examId, List<Answer> answers) {
        var currentUser = userService.getCurrentUser();

        var answeredQuestionIds = answers.stream()
                .map(Answer::getQuestionId)
                .collect(Collectors.toSet());
        var answersForUnansweredQuestions = questionService.getQuestions(examId)
                .stream()
                .filter((question) -> !answeredQuestionIds.contains(question.getId()))
                .map((question) -> Answer.builder()
                        .examId(examId)
                        .questionId(question.getId())
                        .answerText("")
                        .score(0)
                        .submittedAt(LocalDateTime.now())
                        .userId(currentUser.getId())
                        .build())
                .toList();
        answerRepository.insertBatch(answersForUnansweredQuestions);
    }

    private boolean areListsEqual(List<String> listA, List<String> listB) {
        // 如果两个列表长度不同，直接返回 false
        if (listA.size() != listB.size()) {
            return false;
        }

        // 使用 HashMap 统计 listA 中每个元素出现的次数
        var elementCountMap = new HashMap<String, Integer>();
        for (String element : listA) {
            elementCountMap.put(element, elementCountMap.getOrDefault(element, 0) + 1);
        }

        // 遍历 listB，检查每个元素是否与 listA 中的计数匹配
        for (String element : listB) {
            if (!elementCountMap.containsKey(element)) {
                return false; // 如果 listB 中的某个元素不在 listA 中，返回 false
            }

            // 减少元素计数
            elementCountMap.put(element, elementCountMap.get(element) - 1);

            // 如果某个元素的计数减少到 0，移除它
            if (elementCountMap.get(element) == 0) {
                elementCountMap.remove(element);
            }
        }

        // 如果所有元素都匹配，最后 map 应该为空
        return elementCountMap.isEmpty();
    }
}
