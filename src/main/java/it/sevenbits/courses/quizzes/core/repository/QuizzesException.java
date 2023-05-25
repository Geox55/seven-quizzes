package it.sevenbits.courses.quizzes.core.repository;

/**
 * Question exception
 */
public class QuizzesException extends Exception {
    private final QuizzesErrorCode errorCode;

    /**
     * Constructor
     * @param errorCode - describe error
     */
    public QuizzesException(final QuizzesErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Get error
     * @return - error describe
     */
    public QuizzesErrorCode getErrorCode() {
        return errorCode;
    }
}
