package com.ahgtgk.scoresense.service;

import com.ahgtgk.scoresense.config.ConcurrentConfig;
import com.ahgtgk.scoresense.entity.Answer;
import com.ahgtgk.scoresense.entity.ExamResult;
import com.ahgtgk.scoresense.model.biz.BizQuestion;
import com.ahgtgk.scoresense.repository.ExamResultRepository;
import com.ahgtgk.scoresense.view.ReportView;
import com.mybatisflex.core.query.QueryWrapper;
import com.onixbyte.devkit.utils.ChainedCalcUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ExamResultService {

    private final ExamResultRepository examResultRepository;
    private final UserService userService;
    private final AnswerService answerService;
    private final ExamService examService;
    private final QuestionService questionService;

    public ExamResultService(ExamResultRepository examResultRepository,
                             UserService userService,
                             AnswerService answerService,
                             QuestionService questionService,
                             ExamService examService) {
        this.examResultRepository = examResultRepository;
        this.userService = userService;
        this.answerService = answerService;
        this.examService = examService;
        this.questionService = questionService;
    }

    /**
     * 完成考试。
     *
     * @param examId    考试 ID
     * @param vacancyId 岗位 ID
     */
    public ExamResult finishExam(Long examId, Long vacancyId) {
        var currentUser = userService.getCurrentUser();

        // 获取用户的答题信息
        var answers = answerService.getAnswers(examId);

        // 补充用户未回答的题目
        answerService.fillUnansweredQuestions(examId, answers);

        // 计算总分
        var totalScore = answers.stream()
                .mapToInt(Answer::getScore)
                .sum();

        // 构建考试结果信息
        var examResult = ExamResult.builder()
                .examId(examId)
                .userId(currentUser.getId())
                .vacancyId(vacancyId)
                .score(totalScore)
                .build();
        examResultRepository.insertOrUpdate(examResult);
        return examResult;
    }

    /**
     * 获取考试报告。
     */
    public ReportView getReport(Long examId) {
        var currentUser = userService.getCurrentUser();

        // 获取基本数据
        var getExamResultTask = CompletableFuture.supplyAsync(
                () -> examResultRepository.selectOneByQuery(QueryWrapper.create()
                        .where(ExamResult.EXAM_RESULT.USER_ID.eq(currentUser.getId()))
                        .and(ExamResult.EXAM_RESULT.EXAM_ID.eq(examId))),
                ConcurrentConfig.CACHED_EXECUTORS
        );
        var getAnswersTask = CompletableFuture.supplyAsync(
                () -> answerService.getAnswers(examId),
                ConcurrentConfig.CACHED_EXECUTORS
        );
        var getQuestionsTask = CompletableFuture.supplyAsync(
                () -> questionService.getQuestions(examId),
                ConcurrentConfig.CACHED_EXECUTORS
        );
        var getAttendeeCountTask = CompletableFuture.supplyAsync(
                () -> getAttendeeCount(examId, currentUser.getId()),
                ConcurrentConfig.CACHED_EXECUTORS
        );
        var getTotalScoreTask = CompletableFuture.supplyAsync(
                () -> getTotalScore(examId, currentUser.getId()),
                ConcurrentConfig.CACHED_EXECUTORS
        );
        var getLowerCountTask = CompletableFuture.supplyAsync(
                () -> getLowerCount(examId, currentUser.getId()),
                ConcurrentConfig.CACHED_EXECUTORS
        );

        CompletableFuture.allOf(
                getExamResultTask,
                getAnswersTask,
                getQuestionsTask,
                getAttendeeCountTask,
                getTotalScoreTask,
                getLowerCountTask
        ).join();

        var examResult = getExamResultTask.join();
        var questions = getQuestionsTask.join();
        var answers = getAnswersTask.join();

        var reportBuilder = ReportView.builder();

        // 设置考试 ID 和考试名称
        reportBuilder.examId(String.valueOf(examId));
        reportBuilder.examName(examService.getExam(examId).getName());

        // 设置用户 ID
        reportBuilder.userId(String.valueOf(currentUser.getId()));

        // 设置用户总成绩
        reportBuilder.score(examResult.getScore());
        // 设置总计题目数
        reportBuilder.totalQuestionCount(questions.size());
        // 设置考试满分
        var _maxScore = questions.stream()
                .mapToInt(BizQuestion::getMaxScore)
                .sum();
        reportBuilder.totalScore(_maxScore);
        // 设置答对题目书
        var _correctAnswersCount = Long.valueOf(answers.stream()
                .filter((answer) -> answer.getScore() != 0)
                .count()).intValue();
        reportBuilder.correctAnswerCount(_correctAnswersCount);

        // 设置排名
        var _rank = getRank(examId, currentUser.getId());
        reportBuilder.rank(_rank);

        // 设置参考人数
        var _attendeeCount = getAttendeeCountTask.join();
        reportBuilder.attendeeCount(_attendeeCount);

        // 设置平均分
        var _totalScore = getTotalScoreTask.join();
        var _averageScore = _totalScore / _attendeeCount;
        reportBuilder.averageScore(_averageScore);

        // 设置击败考生比例
        var _lowerCount = getLowerCountTask.join();
        var _percentileRank = ChainedCalcUtil.startWith(_lowerCount)
                .divideWithScale(_attendeeCount, 3)
                .getDouble(2);
        reportBuilder.percentileRank(_percentileRank);

        // 设置交卷时间
        reportBuilder.completedAt(examResult.getCompletedAt());

        return reportBuilder.build();
    }

    /**
     * 根据考试 ID 和用户 ID 获取该用户在同考试及同岗位中的排名。
     *
     * @param examId 考试 ID
     * @param userId 用户 ID
     */
    public Integer getRank(Long examId, Long userId) {
        return examResultRepository.selectRank(examId, userId);
    }

    /**
     * 根据考试 ID 和用户 ID 获取同考试同岗位的参考人数。
     */
    public Integer getAttendeeCount(Long examId, Long userId) {
        return examResultRepository.selectAttendeeCount(examId, userId);
    }

    /**
     * 获取指定用户同岗位所有考生的分数之和。
     */
    public Integer getTotalScore(Long examId, Long userId) {
        return examResultRepository.selectTotalScore(examId, userId);
    }

    /**
     * 获取同岗位下分数低于当前用户的用户数量。
     */
    public Integer getLowerCount(Long examId, Long userId) {
        return examResultRepository.selectLowerCount(examId, userId);
    }

}
