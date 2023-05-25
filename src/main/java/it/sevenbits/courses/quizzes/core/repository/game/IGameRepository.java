package it.sevenbits.courses.quizzes.core.repository.game;

import it.sevenbits.courses.quizzes.core.model.game.GameStatus;
import it.sevenbits.courses.quizzes.core.model.question.Question;
import it.sevenbits.courses.quizzes.core.repository.QuizzesException;

import java.util.List;

/**
 * Interface game repository
 */
public interface IGameRepository {
    /**
     * Create game
     *
     * @param roomId   - room id
     * @param playerId player id
     */
    void createGame(String roomId, String playerId);

    /**
     * Game start
     * @param roomId - room id
     * @param playerId - player id
     * @param questionsId - question id
     * @return - question
     * @throws QuizzesException - error
     */
    Question gameStart(String roomId, String playerId, List<String> questionsId) throws QuizzesException;
    /**
     * Get status
     *
     * @param roomId   - room id
     * @param playerId player id
     * @return - game status
     * @throws QuizzesException error
     */
    GameStatus getStatus(String roomId, String playerId) throws QuizzesException;

    /**
     * Update status
     * @param roomId - room id
     */
    void updateStatus(String roomId);

    /**
     * Get total score
     * @param roomId - room id
     * @return - total score
     */
    int getTotalScore(String roomId);

    /**
     * Question score
     * @param roomId - room id
     * @return - question score
     */
    int getQuestionScore(String roomId);

    /**
     * Get next question
     * @param roomId - room id
     * @param playerId player id
     * @return next question id
     */
    String getNextQuestionById(String roomId, String playerId);

    /**
     * finish game
     * @param roomId - room id
     */
    void finish(String roomId);

    /**
     * Get count questions
     * @return count questions
     */
    int getCountQuestions();

    /**
     * Get rules
     * @return rules
     */
    String getRules();

    /**
     * join game
     * @param roomId room id
     * @param playerId player id
     * @return true if true
     * @throws QuizzesException error
     */
    boolean joinGame(String roomId, String playerId) throws QuizzesException;

    /**
     * deleteRoom
     * @param roomId room id
     */
    void delete(String roomId);
}

