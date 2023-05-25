package it.sevenbits.courses.quizzes.core.repository.game;

import it.sevenbits.courses.quizzes.core.model.game.GameStatus;
import it.sevenbits.courses.quizzes.core.model.game.StatusOfGame;
import it.sevenbits.courses.quizzes.core.model.player.PlayerScore;
import it.sevenbits.courses.quizzes.core.model.question.Question;
import it.sevenbits.courses.quizzes.core.repository.QuizzesErrorCode;
import it.sevenbits.courses.quizzes.core.repository.QuizzesException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Game Repository
 */
@Repository
public class GameRepository implements IGameRepository {
    private final JdbcOperations jdbcOperations;
    private Date startDateTime;
    private final int timeLimit;
    private static final int THIRD_COLUMN = 3;
    private static final int FOURTH_COLUMN = 4;
    private static final int FIFTH_COLUMN = 5;
    private static final int SIXTH_COLUMN = 6;
    private static final int COUNT_QUESTIONS_IN_GAME = 4;
    private static final int SECONDS = 30000;

    /**
     * Constructor
     *
     * @param jdbcOperations - interface for database
     */
    public GameRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
        timeLimit = SECONDS * COUNT_QUESTIONS_IN_GAME;
    }

    /**
     * start game
     *
     * @param roomId   - game id
     * @param playerId player id
     */
    @Override
    public void createGame(final String roomId, final String playerId) {
        String gameId = UUID.randomUUID().toString();
        jdbcOperations.update(
                "INSERT INTO games(id_game, id_room, status, questions_count, time_limit)" +
                        " VALUES (?, ?, 'READY_TO_START', 4, ?)",
                gameId, roomId, timeLimit);
        jdbcOperations.update("INSERT INTO player_in_game(id_game, id_player, id_question, question_number, " +
                "total_score) VALUES(?, ?, '', 0, 0)", gameId, playerId);
    }

    /**
     * Start game
     *
     * @param roomId      - room id
     * @param playerId    - player id
     * @param questionsId - question id
     * @return - first question
     * @throws QuizzesException - question exception
     */
    @Override
    public Question gameStart(final String roomId, final String playerId, final List<String> questionsId) throws QuizzesException {
        try {
            jdbcOperations.queryForObject("SELECT id_room, id_player FROM rooms_in_player WHERE id_room = ?",
                    (result, i) -> result.getString(1), roomId);
        } catch (EmptyResultDataAccessException e) {
            throw new QuizzesException(QuizzesErrorCode.ROOM_BY_ID_NOT_FOUND);
        }
        try {
            if (StatusOfGame.fromString(jdbcOperations.queryForObject("SELECT status FROM games where id_room = ?",
                    (result, i) -> result.getString(1), roomId)) == StatusOfGame.IN_PROGRESS) {
                throw new QuizzesException(QuizzesErrorCode.GAME_IS_RUNNING);
            }
        } catch (EmptyResultDataAccessException ignored) {
        }
        startDateTime = new Date();
        jdbcOperations.update("UPDATE games SET status = ?::game_status, start_date_time = ? WHERE id_room = ?",
                StatusOfGame.IN_PROGRESS.getStatus(), startDateTime, roomId);
        String gameId = jdbcOperations.queryForObject("SELECT id_game FROM games WHERE id_room = ?",
                (result, i) -> result.getString(1), roomId);

        Random random = new Random();
        int r = random.nextInt(questionsId.size() - 1);
        Question question = new Question(questionsId.get(r));
        jdbcOperations.update("UPDATE player_in_game SET id_question = ? WHERE id_game = ?", questionsId.get(r), gameId);
        for (int i = 0; i < COUNT_QUESTIONS_IN_GAME; i++) {
            jdbcOperations.update("INSERT INTO questions_in_games(id_game, id_question) VALUES(?, ?)",
                    gameId, questionsId.get(r));
            questionsId.remove(r);
            r = random.nextInt(questionsId.size() - 1);
        }
        return question;
    }

    /**
     * Get status
     *
     * @param roomId   - room id
     * @param playerId player id
     * @return - game status
     */
    @Override
    public GameStatus getStatus(final String roomId, final String playerId) throws QuizzesException {
        List<PlayerScore> playersScore = jdbcOperations.query("SELECT DISTINCT player_in_game.id_player, users.name, "
                        + "player_in_game.total_score " +
                        "FROM player_in_game INNER JOIN games ON games.id_room = ? AND " +
                        "games.id_game = player_in_game.id_game INNER JOIN users ON users.player_id = player_in_game.id_player",
                (result, i) -> new PlayerScore(result.getString(1), result.getString(2),
                        Integer.parseInt(result.getString(THIRD_COLUMN))), roomId);

        try {
            return jdbcOperations.queryForObject("SELECT DISTINCT " +
                    "games.status, games.questions_count, games.start_date_time, games.time_limit, player_in_game.id_question, " +
                    "player_in_game.question_number, player_in_game.id_player, games.id_room FROM rooms_in_player "
                    + "INNER JOIN games ON rooms_in_player.id_room = games.id_room and games.id_room = ? " +
                    "INNER JOIN player_in_game ON player_in_game.id_player = ? and rooms_in_player.id_player = player_in_game.id_player " +
                    "AND games.id_game = player_in_game.id_game", (result, i) ->
                    new GameStatus(StatusOfGame.valueOf(result.getString(1)), result.getString(FIFTH_COLUMN),
                            Integer.parseInt(result.getString(SIXTH_COLUMN)),
                            Integer.parseInt(result.getString(2)),
                            result.getString(THIRD_COLUMN), Integer.parseInt(result.getString(FOURTH_COLUMN)),
                            playersScore), roomId, playerId);
        } catch (EmptyResultDataAccessException e) {
            throw new QuizzesException(QuizzesErrorCode.ROOM_OR_ANSWER_OR_PLAYER_NOT_FOUND);
        }

    }

    /**
     * update score
     *
     * @param playerId - room id
     */
    @Override
    public void updateStatus(final String playerId) {
        int totalScore = getTotalScore(playerId) + getQuestionScore(playerId);
        jdbcOperations.update("UPDATE player_in_game SET total_score = ? WHERE id_player = ?", totalScore, playerId);
    }

    /**
     * get total score
     *
     * @param playerId - room id
     * @return total score
     */
    @Override
    public int getTotalScore(final String playerId) {
        return jdbcOperations.queryForObject("SELECT total_score FROM player_in_game WHERE id_player = ?",
                (result, i) -> Integer.parseInt(result.getString(1)), playerId);
    }

    /**
     * Get question score
     *
     * @param playerId - room id
     * @return - question score
     */
    @Override
    public int getQuestionScore(final String playerId) {
        String questionId = jdbcOperations.queryForObject("SELECT id_question FROM player_in_game WHERE id_player = ?",
                (result, i) -> result.getString(1), playerId);
        return jdbcOperations.queryForObject("SELECT question_score FROM questions WHERE id_question = ?",
                (result, i) -> Integer.parseInt(result.getString(1)), questionId);
    }

    /**
     * get next question by id
     *
     * @param roomId - room id
     * @return next question or null
     */
    @Override
    public String getNextQuestionById(final String roomId, final String playerId) {
        int questionNumber = jdbcOperations.queryForObject("SELECT DISTINCT question_number FROM player_in_game " +
                        "INNER JOIN games ON player_in_game.id_game = games.id_game WHERE id_room = ? AND id_player = ?",
                (result, i) -> Integer.parseInt(result.getString(1)), roomId, playerId) + 1;

        jdbcOperations.update("UPDATE player_in_game SET question_number = ? WHERE id_player = ?",
                questionNumber, playerId);

        List<String> list = jdbcOperations.query("SELECT questions_in_games.id_question FROM player_in_game" +
                        " INNER JOIN questions_in_games ON player_in_game.id_player = ? and" +
                        " player_in_game.id_game = questions_in_games.id_game",
                (result, id) -> result.getString(1), playerId);
        if (questionNumber >= COUNT_QUESTIONS_IN_GAME) {
            return null;
        }
        jdbcOperations.update("UPDATE player_in_game SET id_question = ? WHERE id_player = ?", list.get(questionNumber), playerId);
        return list.get(questionNumber);
    }

    /**
     * finish game
     *
     * @param roomId - room id
     */
    @Override
    public void finish(final String roomId) {
        jdbcOperations.update("UPDATE games SET status = 'FINISHED'::game_status WHERE id_room = ?", roomId);
    }

    /**
     * Delete
     *
     * @param roomId room id
     */
    public void delete(final String roomId) {
        String gameId = jdbcOperations.queryForObject("SELECT id_game FROM games WHERE id_room = ?",
                (result, i) -> result.getString(1), roomId);
        jdbcOperations.update("DELETE FROM questions_in_games WHERE id_game = ?", gameId);
        jdbcOperations.update("DELETE FROM player_in_game WHERE id_game = ?", gameId);
        jdbcOperations.update("DELETE FROM questions_in_games WHERE id_game = ?", roomId);
        jdbcOperations.update("DELETE FROM games WHERE id_room = ?", roomId);
        jdbcOperations.update("DELETE FROM rooms WHERE id_room = ?", roomId);
    }

    /**
     * get count questions
     *
     * @return - count questions
     */
    @Override
    public int getCountQuestions() {
        return COUNT_QUESTIONS_IN_GAME;
    }

    @Override
    public String getRules() {
        return jdbcOperations.queryForObject("SELECT rules FROM rules", (result, i) -> result.getString("rules"));
    }

    /**
     * get owner id
     *
     * @param roomId - room id
     * @return owner id
     */
    public String getOwnerId(final String roomId) {
        return jdbcOperations.queryForObject("SELECT owner FROM rooms WHERE id_room = ?",
                (result, i) -> result.getString(1), roomId);

    }

    /**
     * check has room db
     *
     * @param roomId - room id
     * @return true if has db
     */
    public boolean hasRoom(final String roomId) {
        try {
            jdbcOperations.queryForObject("SELECT id_room FROM rooms WHERE id_room = ?",
                    (result, i) -> result.getString(1), roomId);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    /**
     * join game
     *
     * @param roomId   - id room where exit player
     * @param playerId - player id
     * @return true if success
     */
    public boolean joinGame(final String roomId, final String playerId) {
        String gameId = jdbcOperations.queryForObject("SELECT id_game FROM games WHERE id_room = ?",
                (result, i) -> result.getString(1), roomId);
        jdbcOperations.update("INSERT INTO rooms_in_player(id_room, id_player) VALUES (?, ?)", roomId, playerId);
        return jdbcOperations.update("INSERT INTO player_in_game(id_game, id_player, id_question, question_number, " +
                "total_score) VALUES(?, ? ,'', 0, 0)", gameId, playerId) > 0;

    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public int getTimeLimit() {
        return timeLimit;
    }
}
