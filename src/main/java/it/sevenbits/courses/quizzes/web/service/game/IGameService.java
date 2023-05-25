package it.sevenbits.courses.quizzes.web.service.game;

import it.sevenbits.courses.quizzes.core.model.game.GameStatus;
import it.sevenbits.courses.quizzes.core.model.question.Question;
import it.sevenbits.courses.quizzes.core.model.question.QuestionWithOptions;
import it.sevenbits.courses.quizzes.core.repository.QuizzesException;
import it.sevenbits.courses.quizzes.web.controller.security.UserCredentials;
import it.sevenbits.courses.quizzes.web.model.answer.AnswerQuestionRequest;
import it.sevenbits.courses.quizzes.web.model.answer.AnswerQuestionResponse;

/**
 * Games service
 */
public interface IGameService {
    /**
     * Create game
     *
     * @param roomId   - room id
     * @param playerId player id
     */
    void createGame(String roomId, String playerId);

    /**
     * Game start
     *
     * @param roomId          - room id
     * @param userCredentials - user creadentials
     * @return - question
     * @throws QuizzesException - error
     */
    Question gameStart(String roomId, UserCredentials userCredentials) throws QuizzesException;

    /**
     * Get question by id
     *
     * @param questionId      - question id
     * @param roomId          - room id
     * @param userCredentials user info
     * @return - Question with options
     * @throws QuizzesException - error
     */
    QuestionWithOptions getQuestionById(String questionId, String roomId, UserCredentials userCredentials) throws QuizzesException;

    /**
     * Get correct answer
     *
     * @param roomId                - room id
     * @param questionId            - question id
     * @param answerQuestionRequest - answer request
     * @param userCredentials       - user credentials
     * @return answer response
     * @throws QuizzesException - error
     */
    AnswerQuestionResponse getCorrectAnswer(String roomId, String questionId,
                                            AnswerQuestionRequest answerQuestionRequest,
                                            UserCredentials userCredentials) throws QuizzesException;

    /**
     * Get status by id
     *
     * @param roomId          - room id
     * @param userCredentials user info
     * @return - game status
     * @throws QuizzesException - repository exception
     */
    GameStatus getStatus(String roomId, UserCredentials userCredentials) throws QuizzesException;

    /**
     * Get rules
     *
     * @return rules
     */
    String getRules();

    /**
     * join game
     *
     * @param roomId   room id
     * @param playerId player id
     * @throws QuizzesException error
     */
    void joinGame(String roomId, String playerId) throws QuizzesException;

    /**
     * delete game
     * @param roomId room id
     */
    void deleteGame(String roomId);
}

