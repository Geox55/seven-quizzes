package it.sevenbits.courses.quizzes.core.model.game;

import it.sevenbits.courses.quizzes.core.model.player.PlayerScore;

import java.util.ArrayList;
import java.util.List;

/**
 * Game status
 */
public class GameStatus {
    private StatusOfGame status;
    private String questionId;
    private int questionNumber;
    private int questionsCount;
    private List<PlayerScore> playersScore;
    private String startDateTime;
    private int timeLimit;

    /**
     * Empty constructor
     */
    public GameStatus() {

    }

    /**
     * Constructor
     *
     * @param status         - status
     * @param questionId     - question id
     * @param questionNumber - question number
     * @param questionsCount - question count
     * @param playersScore   players score
     * @param startDateTime  start date time
     * @param timeLimit      time limit
     */
    public GameStatus(final StatusOfGame status, final String questionId, final int questionNumber, final int questionsCount,
                      final String startDateTime, final int timeLimit, final List<PlayerScore> playersScore) {
        this.status = status;
        this.questionId = questionId;
        this.questionNumber = questionNumber;
        this.questionsCount = questionsCount;
        this.playersScore = playersScore;
        this.startDateTime = startDateTime;
        this.timeLimit = timeLimit;
    }

    /**
     * constructor
     *
     * @param status         status
     * @param questionId     question id
     * @param questionNumber question number
     * @param startDateTime  start date time
     * @param timeLimit      time limit
     * @param questionsCount questions count
     * @param playerScore    players score
     */
    public GameStatus(final StatusOfGame status, final String questionId, final int questionNumber,
                      final String startDateTime, final int timeLimit, final int questionsCount,
                      final PlayerScore playerScore) {
        this.status = status;
        this.questionId = questionId;
        this.questionNumber = questionNumber;
        this.questionsCount = questionsCount;
        this.playersScore = new ArrayList<>();
        playersScore.add(playerScore);
        this.startDateTime = startDateTime;
        this.timeLimit = timeLimit;
    }

    public StatusOfGame getStatus() {
        return status;
    }

    public void setStatus(final StatusOfGame status) {
        this.status = status;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(final String questionId) {
        this.questionId = questionId;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(final int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public int getQuestionsCount() {
        return questionsCount;
    }

    public void setQuestionsCount(final int questionsCount) {
        this.questionsCount = questionsCount;
    }

    public List<PlayerScore> getPlayersScore() {
        return playersScore;
    }

    public void setPlayersScore(final List<PlayerScore> playersScore) {
        this.playersScore = playersScore;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(final String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(final int timeLimit) {
        this.timeLimit = timeLimit;
    }
}
