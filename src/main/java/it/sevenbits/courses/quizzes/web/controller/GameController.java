package it.sevenbits.courses.quizzes.web.controller;

import it.sevenbits.courses.quizzes.core.model.game.GameStatus;
import it.sevenbits.courses.quizzes.core.model.question.Question;
import it.sevenbits.courses.quizzes.core.model.question.QuestionWithOptions;
import it.sevenbits.courses.quizzes.core.repository.QuizzesErrorCode;
import it.sevenbits.courses.quizzes.core.repository.QuizzesException;
import it.sevenbits.courses.quizzes.web.controller.security.AuthRoleRequired;
import it.sevenbits.courses.quizzes.web.controller.security.UserCredentials;
import it.sevenbits.courses.quizzes.web.model.answer.AnswerQuestionRequest;
import it.sevenbits.courses.quizzes.web.model.answer.AnswerQuestionResponse;
import it.sevenbits.courses.quizzes.web.model.rules.RulesResponse;
import it.sevenbits.courses.quizzes.web.service.game.IGameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * GameController - controller
 */
@RestController
public class GameController {
    private final IGameService gameService;

    /**
     * Constructor
     *
     * @param gameService - game service
     */
    public GameController(final IGameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Get rules
     *
     * @return rules
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("rules")
    public ResponseEntity<RulesResponse> getRules() {
        return ResponseEntity.ok().body(new RulesResponse(gameService.getRules()));
    }

    /**
     * game start
     *
     * @param roomId          - id room
     * @param userCredentials - player id
     * @return id first question
     */
    @CrossOrigin("http://localhost:3000")
    @PostMapping("rooms/{roomId}/game/start")
    @AuthRoleRequired("USER")
    public ResponseEntity<Question> gameStart(@PathVariable(value = "roomId") final String roomId,
                                              final UserCredentials userCredentials) {
        try {
            return ResponseEntity.ok().body(gameService.gameStart(roomId, userCredentials));
        } catch (QuizzesException e) {
            if (e.getErrorCode() == QuizzesErrorCode.ROOM_BY_ID_NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else if (e.getErrorCode() == QuizzesErrorCode.GAME_IS_RUNNING) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * get status about game
     * @param userCredentials user info
     * @param roomId - room id
     * @return response game status
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("rooms/{roomId}/game")
    @AuthRoleRequired("USER")
    public ResponseEntity<GameStatus> getStatus(@PathVariable(value = "roomId") final String roomId,
                                                final UserCredentials userCredentials) {
        try {
            return ResponseEntity.ok().body(gameService.getStatus(roomId, userCredentials));
        } catch (QuizzesException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * get id question
     *
     * @param roomId     - id room
     * @param questionId - id question
     * @param userCredentials user info
     * @return question options
     */
    @CrossOrigin("http://localhost:3000")
    @GetMapping("rooms/{roomId}/game/question/{questionId}")
    @AuthRoleRequired("USER")
    public ResponseEntity<QuestionWithOptions> getQuestionId(@PathVariable(value = "roomId") final String roomId,
                                                             @PathVariable(value = "questionId") final String questionId,
                                                             final UserCredentials userCredentials) {
        try {
            return ResponseEntity.ok().body(gameService.getQuestionById(questionId, roomId, userCredentials));
        } catch (QuizzesException e) {
            if (e.getErrorCode() == QuizzesErrorCode.ROOM_OR_QUESTION_NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * answerQuestion
     *
     * @param roomId                - room id
     * @param questionId            - questionId
     * @param answerQuestionRequest - request answer
     * @param userCredentials       - user credentials
     * @return response answer
     */
    @CrossOrigin("http://localhost:3000")
    @PostMapping(value = "rooms/{roomId}/game/question/{questionId}/answer")
    @AuthRoleRequired("USER")
    public ResponseEntity<AnswerQuestionResponse> answerQuestion(@PathVariable(value = "roomId") final String roomId,
                                                                 @PathVariable(value = "questionId") final String questionId,
                                                                 @RequestBody final AnswerQuestionRequest answerQuestionRequest,
                                                                 final UserCredentials userCredentials) {
        try {
            AnswerQuestionResponse answerQuestionResponse =
                    gameService.getCorrectAnswer(roomId, questionId, answerQuestionRequest, userCredentials);
            if (answerQuestionResponse.getCorrectAnswerId() == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.ok().body(answerQuestionResponse);
        } catch (QuizzesException e) {
            if (e.getErrorCode() == QuizzesErrorCode.ROOM_OR_ANSWER_OR_PLAYER_NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * join Game
     * @param roomId - room id
     * @param userCredentials - user info
     * @return status
     * @throws QuizzesException exception join
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "rooms/{roomId}/join")
    @AuthRoleRequired("USER")
    public ResponseEntity<?> joinGame(@PathVariable(value = "roomId") final String roomId,
                                      final UserCredentials userCredentials) throws QuizzesException {
        gameService.joinGame(roomId, userCredentials.getPlayerId());
        return ResponseEntity.ok().build();
    }

    /**
     * delete game
     * @param roomId room id
     * @return response ok
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "rooms/{roomId}/delete")
    @AuthRoleRequired("USER")
    public ResponseEntity<?> deleteGame(@PathVariable(value = "roomId") final String roomId) {
        gameService.deleteGame(roomId);
        return ResponseEntity.ok().build();
    }
}
