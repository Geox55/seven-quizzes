package it.sevenbits.courses.quizzes.core.repository.question;

import it.sevenbits.courses.quizzes.core.model.question.Question;
import it.sevenbits.courses.quizzes.core.model.question.QuestionAnswer;
import it.sevenbits.courses.quizzes.core.model.question.QuestionWithOptions;
import it.sevenbits.courses.quizzes.core.repository.QuizzesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class QuestionRepositoryTest {
    private JdbcOperations mockJdbcOperations;
    private QuestionRepository databaseRepository;

    @BeforeEach
    public void setup() {
        mockJdbcOperations = mock(JdbcOperations.class);
        databaseRepository = new QuestionRepository(mockJdbcOperations);
    }

    @Test
    void getQuestionById() throws QuizzesException {
        String questionId = "1";
        String roomId = "1";
        String playerId = "1";
        List list = mock(List.class);
        QuestionWithOptions mockQuestionWithOptions = mock(QuestionWithOptions.class);
        Question mockQuestion = mock(Question.class);
        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), anyString())).thenReturn("1").thenReturn(mockQuestionWithOptions);
        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), anyString(), anyString())).thenReturn(mockQuestion);
        when(mockJdbcOperations.query(anyString(), any(RowMapper.class), anyString())).thenReturn(list);

        QuestionWithOptions question = databaseRepository.getQuestionById(questionId, roomId, playerId);

        verify(mockJdbcOperations, times(1)).queryForObject(eq("SELECT id_game FROM games WHERE id_room = ?"), any(RowMapper.class), eq(roomId));
        verify(mockJdbcOperations, times(1)).queryForObject(eq("SELECT id_question FROM player_in_game WHERE id_player = ? AND id_game = ?"), any(RowMapper.class), eq(playerId), eq(roomId));
        verify(mockJdbcOperations, times(1)).query(eq("SELECT DISTINCT answers.id_answer, answers.text FROM answers inner join questions on answers.id_question = ?"), any(RowMapper.class), eq(questionId));
        verify(mockJdbcOperations, times(1)).queryForObject(eq("SELECT id_question, text FROM questions WHERE id_question = ?"), any(RowMapper.class), eq(questionId));
        assertSame(mockQuestionWithOptions, question);
    }

    @Test
    void getCorrectAnswer() {
        try {
            String roomId = "2";
            String playerId = "3";
            String questionId = "1";
            QuestionAnswer mockQuestionAnswer = mock(QuestionAnswer.class);
            Question mockQuestion = mock(Question.class);
            when(mockQuestion.getQuestionId()).thenReturn(questionId);
            when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), anyString())).thenReturn(roomId).thenReturn(questionId).thenReturn(mockQuestion).thenReturn(mockQuestionAnswer);
            when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), anyString(), anyString())).thenReturn(playerId);

            QuestionAnswer questionAnswer = databaseRepository.getCorrectAnswer(roomId, questionId, playerId);

            verify(mockJdbcOperations, times(1)).queryForObject(eq("SELECT id_room FROM rooms WHERE id_room = ?"), any(RowMapper.class), eq(roomId));
            verify(mockJdbcOperations, times(1)).queryForObject(eq("SELECT id_player FROM rooms_in_player WHERE id_room = ? and id_player = ?"), any(RowMapper.class), eq(roomId), eq(playerId));
            verify(mockJdbcOperations, times(1)).queryForObject(eq("SELECT id_question FROM questions WHERE id_question = ?"), any(RowMapper.class), eq(questionId));
            verify(mockJdbcOperations, times(1)).queryForObject(eq("SELECT id_question FROM player_in_game WHERE id_player = ?"), any(RowMapper.class), eq(playerId));
            verify(mockJdbcOperations, times(1)).queryForObject(eq("SELECT DISTINCT questions.id_correct_answer, answers.text FROM questions INNER JOIN answers ON " +
                    "questions.id_question = answers.id_question " +
                    "WHERE questions.id_question = ? and questions.id_correct_answer = answers.id_answer"), any(RowMapper.class), eq(questionId));
            assertSame(mockQuestionAnswer, questionAnswer);
        } catch (QuizzesException e) {
            System.out.println(e.getErrorCode().getError());
            fail();
        }
    }

}