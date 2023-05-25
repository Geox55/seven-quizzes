package it.sevenbits.courses.quizzes.web.service.game;

import it.sevenbits.courses.quizzes.core.model.game.GameStatus;
import it.sevenbits.courses.quizzes.core.model.game.StatusOfGame;
import it.sevenbits.courses.quizzes.core.model.question.Question;
import it.sevenbits.courses.quizzes.core.model.question.QuestionAnswer;
import it.sevenbits.courses.quizzes.core.model.question.QuestionWithOptions;
import it.sevenbits.courses.quizzes.core.repository.game.GameRepository;
import it.sevenbits.courses.quizzes.core.repository.QuizzesException;
import it.sevenbits.courses.quizzes.core.repository.question.QuestionRepository;
import it.sevenbits.courses.quizzes.web.controller.security.UserCredentials;
import it.sevenbits.courses.quizzes.web.model.answer.AnswerQuestionRequest;
import it.sevenbits.courses.quizzes.web.model.answer.AnswerQuestionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class GameServiceTest {
    private GameRepository mockGameRepository;
    private QuestionRepository mockQuestionRepository;
    private GameService gameService;

    @BeforeEach
    public void setup() {
        mockGameRepository = mock(GameRepository.class);
        mockQuestionRepository = mock(QuestionRepository.class);
        gameService = new GameService(mockGameRepository, mockQuestionRepository);
    }
    @Test
    void gameStart() {
        try {
            Question mockQuestion = mock(Question.class);
            String roomId = "1";
            String playerId = "2";
            UserCredentials mockUserCredentials = mock(UserCredentials.class);
            List<String> list = mock(List.class);
            when(mockQuestionRepository.getListQuestion()).thenReturn(list);
            when(mockGameRepository.gameStart(roomId, playerId, list)).thenReturn(mockQuestion);
            when(mockUserCredentials.getPlayerId()).thenReturn(playerId);

            Question answer = gameService.gameStart(roomId, mockUserCredentials);
            verify(mockQuestionRepository, times(1)).getListQuestion();
            verify(mockGameRepository, times(1)).gameStart(roomId, playerId, list);
            assertSame(mockQuestion, answer);
        } catch (QuizzesException e) {
            fail();
        }

    }

    @Test
    void getQuestionById() {
        try {
            String questionId = "1";
            String roomId = "2";
            String playerId = "3";
            UserCredentials userCredentials = mock(UserCredentials.class);
            GameStatus mockGameStatus = mock(GameStatus.class);
            QuestionWithOptions mockQuestionWithOptions = mock(QuestionWithOptions.class);
            when(userCredentials.getPlayerId()).thenReturn(playerId).thenReturn(playerId);
            when(mockGameStatus.getStatus()).thenReturn(StatusOfGame.READY_TO_START);
            when(mockGameRepository.getStatus(roomId, playerId)).thenReturn(mockGameStatus);
            when(mockQuestionRepository.getQuestionById(questionId, roomId, playerId)).thenReturn(mockQuestionWithOptions);

            QuestionWithOptions answer = gameService.getQuestionById(questionId, roomId, userCredentials);

            verify(mockQuestionRepository, times(1)).getQuestionById(questionId, roomId, playerId);
            assertSame(mockQuestionWithOptions, answer);
        } catch (QuizzesException e) {
            fail();
        }

    }

    @Test
    void getCorrectAnswer() {
        try {
            AnswerQuestionRequest mockQuestionRequest = mock(AnswerQuestionRequest.class);
            QuestionAnswer mockQuestionAnswer = mock(QuestionAnswer.class);
            UserCredentials userCredentials = mock(UserCredentials.class);
            String roomId = "1";
            String questionId = "2";
            String playerId = "3";
            String nextQuestionId = "4";
            String answerId = "5";
            int totalScore = 1;
            int questionScore = 2;

            when(mockQuestionAnswer.getAnswerId()).thenReturn(answerId).thenReturn(answerId);
            when(mockGameRepository.getTotalScore(playerId)).thenReturn(totalScore);
            when(mockGameRepository.getQuestionScore(playerId)).thenReturn(questionScore);
            when(mockQuestionRequest.getAnswerId()).thenReturn(answerId);
            when(userCredentials.getPlayerId()).thenReturn(playerId).thenReturn(playerId).thenReturn(playerId).thenReturn(playerId).thenReturn(playerId).thenReturn(playerId);
            when(mockGameRepository.getNextQuestionById(roomId, playerId)).thenReturn(nextQuestionId);
            when(mockQuestionRepository.getCorrectAnswer(roomId, questionId, playerId)).thenReturn(mockQuestionAnswer);

            AnswerQuestionResponse answer = gameService.getCorrectAnswer(roomId, questionId, mockQuestionRequest, userCredentials);

            verify(mockQuestionRepository, times(1)).getCorrectAnswer(roomId, questionId, playerId);
            verify(mockQuestionAnswer, times(2)).getAnswerId();
            verify(mockGameRepository, times(1)).getTotalScore(playerId);
            verify(mockGameRepository, times(1)).getQuestionScore(playerId);
            verify(mockQuestionRequest, times(1)).getAnswerId();
            verify(userCredentials, times(5)).getPlayerId();
            verify(mockGameRepository, times(1)).getNextQuestionById(roomId, playerId);

            assertEquals(new AnswerQuestionResponse(answerId, nextQuestionId, totalScore, questionScore), answer);
        } catch (QuizzesException e) {
            fail();
        }
    }
}