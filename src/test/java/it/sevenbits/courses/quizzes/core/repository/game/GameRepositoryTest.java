package it.sevenbits.courses.quizzes.core.repository.game;

import it.sevenbits.courses.quizzes.core.model.game.StatusOfGame;
import it.sevenbits.courses.quizzes.core.model.question.Question;
import it.sevenbits.courses.quizzes.core.repository.QuizzesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class GameRepositoryTest {
    private JdbcOperations mockJdbcOperations;
    private GameRepository databaseRepository;

    @BeforeEach
    public void setup() {
        mockJdbcOperations = mock(JdbcOperations.class);
        databaseRepository = new GameRepository(mockJdbcOperations);
    }

    @Test
    void createGameTest() {
        String playerId = "1";
        String roomId = "2";
        databaseRepository.createGame(roomId, playerId);
        verify(mockJdbcOperations, times(1)).update(eq("INSERT INTO games(id_game, id_room, status, questions_count, time_limit) VALUES (?, ?, 'READY_TO_START', 4, ?)"), any(), eq(roomId), any());
        verify(mockJdbcOperations, times(1)).update(eq("INSERT INTO player_in_game(id_game, id_player, id_question, question_number, total_score) VALUES(?, ?, '', 0, 0)"), any(), eq(playerId));
    }

    @Test
    void gameStartTest() throws QuizzesException {
        String roomId = "1";
        String questionId = "2";
        String gameId = "3";
        String playerId = "4";
        List<String> list = mock(List.class);
        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), anyString())).thenReturn(gameId).thenReturn(gameId).thenReturn(gameId);
        when(list.get(anyInt())).thenReturn(questionId).thenReturn(questionId).thenReturn(questionId).thenReturn(questionId).thenReturn(questionId);
        when(list.size()).thenReturn(4);

        Question answer = databaseRepository.gameStart(roomId, playerId, list);

        verify(mockJdbcOperations, times(1)).queryForObject(eq("SELECT id_room, id_player FROM rooms_in_player WHERE id_room = ?"), any(RowMapper.class), eq(roomId));
        verify(mockJdbcOperations, times(1)).queryForObject(eq("SELECT status FROM games where id_room = ?"), any(RowMapper.class), eq(roomId));
        verify(mockJdbcOperations, times(1)).update(eq("UPDATE games SET status = ?::game_status, start_date_time = ? WHERE id_room = ?"), any(), any(), eq(roomId));
        verify(mockJdbcOperations, times(1)).queryForObject(eq("SELECT id_game FROM games WHERE id_room = ?"), any(RowMapper.class), eq(roomId));
        verify(mockJdbcOperations, times(1)).update(eq("UPDATE player_in_game SET id_question = ? WHERE id_game = ?"),
                eq(questionId), eq(gameId));
        verify(mockJdbcOperations, times(4)).update(eq("INSERT INTO questions_in_games(id_game, id_question) VALUES(?, ?)"), eq(gameId), eq(questionId));
        assertEquals(new Question(questionId), answer);
    }
}