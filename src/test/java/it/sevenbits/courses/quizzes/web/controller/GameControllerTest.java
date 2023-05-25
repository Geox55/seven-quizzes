package it.sevenbits.courses.quizzes.web.controller;

import it.sevenbits.courses.quizzes.core.model.question.Question;
import it.sevenbits.courses.quizzes.core.model.question.QuestionWithOptions;
import it.sevenbits.courses.quizzes.core.repository.QuizzesException;
import it.sevenbits.courses.quizzes.web.controller.security.UserCredentials;
import it.sevenbits.courses.quizzes.web.model.answer.AnswerQuestionRequest;
import it.sevenbits.courses.quizzes.web.model.answer.AnswerQuestionResponse;
import it.sevenbits.courses.quizzes.web.service.game.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameControllerTest {

    private GameService mockGameService;
    private GameController gameController;

    @BeforeEach
    public void setup() {
        mockGameService = mock(GameService.class);
        gameController = new GameController(mockGameService);
    }

    @Test
    void gameStart() {
        try {
            Question mockQuestion = mock(Question.class);
            UserCredentials mockUserCredentials = mock(UserCredentials.class);
            String roomId = "1";
            String playerId = "2";
            when(mockGameService.gameStart(roomId, mockUserCredentials)).thenReturn(mockQuestion);
            when(mockUserCredentials.getPlayerId()).thenReturn(playerId);

            ResponseEntity<Question> answer = gameController.gameStart(roomId, mockUserCredentials);
            verify(mockGameService, times(1)).gameStart(roomId, mockUserCredentials);
            assertEquals(HttpStatus.OK, answer.getStatusCode());
            assertSame(mockQuestion, answer.getBody());
        } catch (QuizzesException e) {
            fail();
        }
    }

    @Test
    void getQuestionId() {
        try {
            QuestionWithOptions mockQuestionWithOptions = mock(QuestionWithOptions.class);
            UserCredentials mockUserCredentials = mock(UserCredentials.class);
            String roomId = "1";
            String questionId = "2";
            when(mockGameService.getQuestionById(questionId, roomId, mockUserCredentials)).thenReturn(mockQuestionWithOptions);

            ResponseEntity<QuestionWithOptions> answer = gameController.getQuestionId(roomId, questionId, mockUserCredentials);

            verify(mockGameService, times(1)).getQuestionById(questionId, roomId, mockUserCredentials);
            assertEquals(HttpStatus.OK, answer.getStatusCode());
            assertSame(mockQuestionWithOptions, answer.getBody());
        } catch (QuizzesException e) {
            fail();
        }
    }

    @Test
    void answerQuestion() {
        try {
            String roomId = "1";
            String questionId = "2";
            String correctAnswer = "3";
            AnswerQuestionResponse mockAnswerQuestionResponse = mock(AnswerQuestionResponse.class);
            AnswerQuestionRequest mockAnswerQuestionRequest = mock(AnswerQuestionRequest.class);
            UserCredentials userCredentials = mock(UserCredentials.class);
            when(mockGameService.getCorrectAnswer(roomId, questionId, mockAnswerQuestionRequest, userCredentials)).thenReturn(mockAnswerQuestionResponse);
            when(mockAnswerQuestionResponse.getCorrectAnswerId()).thenReturn(correctAnswer);
            ResponseEntity<AnswerQuestionResponse> answer = gameController.answerQuestion(roomId, questionId, mockAnswerQuestionRequest, userCredentials);
            verify(mockGameService, times(1)).getCorrectAnswer(roomId, questionId, mockAnswerQuestionRequest, userCredentials);
            assertEquals(HttpStatus.OK, answer.getStatusCode());
            assertSame(mockAnswerQuestionResponse, answer.getBody());
        } catch (QuizzesException e) {
            fail();
        }
    }
}