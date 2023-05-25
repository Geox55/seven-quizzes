package it.sevenbits.courses.quizzes.web.service.game;

import it.sevenbits.courses.quizzes.core.model.game.GameStatus;
import it.sevenbits.courses.quizzes.core.model.game.StatusOfGame;
import it.sevenbits.courses.quizzes.core.model.question.Question;
import it.sevenbits.courses.quizzes.core.model.question.QuestionAnswer;
import it.sevenbits.courses.quizzes.core.model.question.QuestionWithOptions;
import it.sevenbits.courses.quizzes.core.repository.QuizzesErrorCode;
import it.sevenbits.courses.quizzes.core.repository.QuizzesException;
import it.sevenbits.courses.quizzes.core.repository.game.IGameRepository;
import it.sevenbits.courses.quizzes.core.repository.question.IQuestionRepository;
import it.sevenbits.courses.quizzes.timer.Timer;
import it.sevenbits.courses.quizzes.web.controller.security.UserCredentials;
import it.sevenbits.courses.quizzes.web.model.answer.AnswerQuestionRequest;
import it.sevenbits.courses.quizzes.web.model.answer.AnswerQuestionResponse;
import org.springframework.stereotype.Service;

/**
 * Game Service
 */
@Service
public class GameService implements IGameService {
    private final IGameRepository gameRepository;
    private final IQuestionRepository questionRepository;

    /**
     * game Service - constructor
     *
     * @param gameRepository     - repository game
     * @param questionRepository - repository question
     */
    public GameService(final IGameRepository gameRepository, final IQuestionRepository questionRepository) {
        this.gameRepository = gameRepository;
        this.questionRepository = questionRepository;
    }

    /**
     * create game
     *
     * @param roomId   - room id
     * @param playerId player id
     */
    @Override
    public void createGame(final String roomId, final String playerId) {
        gameRepository.createGame(roomId, playerId);
    }

    /**
     * game Start
     *
     * @param roomId          - room id
     * @param userCredentials - user credential player id
     * @return - first question
     * @throws QuizzesException - question exception
     */
    @Override
    public Question gameStart(final String roomId, final UserCredentials userCredentials) throws QuizzesException {
        if (roomId == null || roomId.equals("") || userCredentials == null || "".equals(userCredentials.getPlayerId())) {
            throw new QuizzesException(QuizzesErrorCode.INVALID_INPUT);
        }
        Timer timer = new Timer(gameRepository, roomId);
        timer.start();
        return gameRepository.gameStart(roomId, userCredentials.getPlayerId(), questionRepository.getListQuestion());
    }

    /**
     * getQuestionById - get id question
     *
     * @param questionId      - id question
     * @param roomId          - id room
     * @param userCredentials user info
     * @return question options
     * @throws QuizzesException - question exception
     */
    @Override
    public QuestionWithOptions getQuestionById(final String questionId, final String roomId,
                                               final UserCredentials userCredentials) throws QuizzesException {
        if ("".equals(questionId) || "".equals(roomId)) {
            throw new QuizzesException(QuizzesErrorCode.INVALID_INPUT);
        }
        if (gameRepository.getStatus(roomId, userCredentials.getPlayerId()).getStatus() == StatusOfGame.FINISHED) {
            throw new QuizzesException(QuizzesErrorCode.GAME_IS_FINISHED);
        }
        return questionRepository.getQuestionById(questionId, roomId, userCredentials.getPlayerId());
    }

    /**
     * get correct answer
     *
     * @param roomId                - room id
     * @param questionId            - question id
     * @param answerQuestionRequest - request answer
     * @param userCredentials       - user credentials
     * @return response answer
     * @throws QuizzesException - question exception
     */
    @Override
    public AnswerQuestionResponse getCorrectAnswer(final String roomId, final String questionId,
                                                   final AnswerQuestionRequest answerQuestionRequest,
                                                   final UserCredentials userCredentials) throws QuizzesException {
        try {
            QuestionAnswer correctAnswer = questionRepository.getCorrectAnswer(roomId, questionId, userCredentials.getPlayerId());
            if (correctAnswer.getAnswerId().equals(answerQuestionRequest.getAnswerId())) {
                gameRepository.updateStatus(userCredentials.getPlayerId());
            }
            String nextQuestionId = gameRepository.getNextQuestionById(roomId, userCredentials.getPlayerId());
            if (nextQuestionId == null) {
                gameRepository.finish(roomId);
            }
            return new AnswerQuestionResponse(correctAnswer.getAnswerId(), nextQuestionId,
                    gameRepository.getTotalScore(userCredentials.getPlayerId()),
                    gameRepository.getQuestionScore(userCredentials.getPlayerId()));
        } catch (QuizzesException e) {
            if (e.getErrorCode() == QuizzesErrorCode.QUESTION_NOT_CURRENT_IN_GAME) {
                return new AnswerQuestionResponse(questionId, gameRepository.getTotalScore(userCredentials.getPlayerId()));
            }
            throw e;
        }
    }

    /**
     * get status
     *
     * @param roomId          - room id
     * @param userCredentials user info
     * @return - game status
     */
    @Override
    public GameStatus getStatus(final String roomId, final UserCredentials userCredentials) throws QuizzesException {
        GameStatus gameStatus = gameRepository.getStatus(roomId, userCredentials.getPlayerId());
        if (gameStatus == null) {
            throw new QuizzesException(QuizzesErrorCode.ROOM_BY_ID_NOT_FOUND);
        }
        return gameStatus;
    }

    @Override
    public String getRules() {
        return gameRepository.getRules();
    }

    /**
     * join game
     *
     * @param roomId   - string room id
     * @param playerId - player id
     * @throws QuizzesException exception join
     */
    public void joinGame(final String roomId, final String playerId) throws QuizzesException {
        gameRepository.joinGame(roomId, playerId);
    }

    @Override
    public void deleteGame(final String roomId) {
        gameRepository.finish(roomId);
        gameRepository.delete(roomId);
    }
}
