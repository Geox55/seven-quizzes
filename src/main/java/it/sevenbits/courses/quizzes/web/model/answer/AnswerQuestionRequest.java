package it.sevenbits.courses.quizzes.web.model.answer;


/**
 * Answer Question Request
 */
public class AnswerQuestionRequest {
    private String answerId;

    /**
     * Empty constructor
     */
    public AnswerQuestionRequest() {
    }

    /**
     * Constructor
     * @param answerId - answer id
     */
    public AnswerQuestionRequest(final String answerId) {
        this.answerId = answerId;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(final String answerId) {
        this.answerId = answerId;
    }
}
