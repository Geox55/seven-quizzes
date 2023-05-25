package it.sevenbits.courses.quizzes.core.model.question;

import java.util.Objects;

/**
 * Question
 */
public class Question {
    private String questionId;

    /**
     * constructor
     * @param questionId - question id
     */
    public Question(final String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(final String questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        return Objects.equals(questionId, question.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId);
    }
}
