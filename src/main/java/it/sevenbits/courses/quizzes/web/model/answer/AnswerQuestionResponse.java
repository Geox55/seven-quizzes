package it.sevenbits.courses.quizzes.web.model.answer;

import java.io.Serializable;
import java.util.Objects;

/**
 * Answer Question Response
 */
public class AnswerQuestionResponse implements Serializable {
    private String correctAnswerId;
    private String questionId;
    private int totalScore;
    private int questionScore;

    /**
     * Empty constructor
     */
    public AnswerQuestionResponse() {
    }

    /**
     * Constructor
     *
     * @param correctAnswerId - correct answer id
     * @param nextQuestionId  - next question id
     * @param totalScore      - total score
     * @param questionScore   - question score
     */
    public AnswerQuestionResponse(final String correctAnswerId, final String nextQuestionId, final int totalScore,
                                  final int questionScore) {
        this.correctAnswerId = correctAnswerId;
        this.questionId = nextQuestionId;
        this.totalScore = totalScore;
        this.questionScore = questionScore;
    }

    /**
     * Constructor for error
     * @param questionId - question id
     * @param totalScore - total score
     */
    public AnswerQuestionResponse(final String questionId, final int totalScore) {
        correctAnswerId = null;
        this.questionId = questionId;
        this.totalScore = totalScore;
        questionScore = 0;
    }

    /**
     * get correct answer id
     *
     * @return - correct answer id
     */
    public String getCorrectAnswerId() {
        return correctAnswerId;
    }

    /**
     * get next question id
     *
     * @return next question
     */
    public String getQuestionId() {
        return questionId;
    }

    /**
     * get total score
     *
     * @return total score
     */
    public int getTotalScore() {
        return totalScore;
    }

    public int getQuestionScore() {
        return questionScore;
    }

    @Override
    public String toString() {
        return "AnswerQuestionResponse{" +
                "correctAnswerId='" + correctAnswerId + '\'' +
                ", nextQuestionId='" + questionId + '\'' +
                ", totalScore=" + totalScore +
                ", questionScore=" + questionScore +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnswerQuestionResponse that = (AnswerQuestionResponse) o;
        return totalScore == that.totalScore && questionScore == that.questionScore &&
                Objects.equals(correctAnswerId, that.correctAnswerId) && Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correctAnswerId, questionId, totalScore, questionScore);
    }
}
