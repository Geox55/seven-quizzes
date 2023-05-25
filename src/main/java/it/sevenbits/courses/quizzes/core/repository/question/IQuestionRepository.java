package it.sevenbits.courses.quizzes.core.repository.question;

import it.sevenbits.courses.quizzes.core.model.question.QuestionAnswer;
import it.sevenbits.courses.quizzes.core.model.question.QuestionWithOptions;
import it.sevenbits.courses.quizzes.core.repository.QuizzesException;

import java.util.List;

/**
 * Interface question repository
 */
public interface IQuestionRepository {
    /**
     * Get list question
     *
     * @return - list of string
     */
    List<String> getListQuestion();

    /**
     * Get question by id
     *
     * @param questionId - question id
     * @param roomId     - room id
     * @param playerId   player id
     * @return - question with options
     * @throws QuizzesException - error
     */
    QuestionWithOptions getQuestionById(String questionId, String roomId, String playerId) throws QuizzesException;

    /**
     * Get correct answer
     *
     * @param roomId     - room id
     * @param questionId - question id
     * @param playerId   - player id
     * @return - question answer
     * @throws QuizzesException - error
     */
    QuestionAnswer getCorrectAnswer(String roomId, String questionId, String playerId) throws QuizzesException;
}

