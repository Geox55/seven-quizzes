package it.sevenbits.courses.quizzes.core.repository.question;

import it.sevenbits.courses.quizzes.core.model.question.Question;
import it.sevenbits.courses.quizzes.core.model.question.QuestionAnswer;
import it.sevenbits.courses.quizzes.core.model.question.QuestionWithOptions;
import it.sevenbits.courses.quizzes.core.repository.QuizzesErrorCode;
import it.sevenbits.courses.quizzes.core.repository.QuizzesException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Question repository
 */
@Repository
public class QuestionRepository implements IQuestionRepository {
    private final JdbcOperations jdbcOperations;

    /**
     * Constructor
     *
     * @param jdbcOperations - interface for database
     */
    public QuestionRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    /**
     * Get list of question
     *
     * @return - list of ids question
     */
    @Override
    public List<String> getListQuestion() {
        return jdbcOperations.query("SELECT id_question FROM questions", (result, i) -> {
            return result.getString(1);
        });
    }

    /**
     * Get question by id
     *
     * @param questionId - question id
     * @param roomId     - room id
     * @param playerId
     * @return - questionWithOptions
     * @throws QuizzesException - question exception
     */
    @Override
    public QuestionWithOptions getQuestionById(final String questionId, final String roomId, final String playerId)
            throws QuizzesException {
        try {
            String gameId = jdbcOperations.queryForObject("SELECT id_game FROM games WHERE id_room = ?",
                    (result, i) -> result.getString(1), roomId);
            Question question = jdbcOperations.queryForObject("SELECT id_question FROM player_in_game WHERE id_player = ? AND id_game = ?",
                    (result, i) -> new Question(result.getString(1)), playerId, gameId);
        } catch (EmptyResultDataAccessException e) {
            throw new QuizzesException(QuizzesErrorCode.ROOM_OR_QUESTION_NOT_FOUND);
        }

        List<QuestionAnswer> answers = jdbcOperations.query(
                "SELECT DISTINCT answers.id_answer, answers.text FROM answers inner join questions on answers.id_question = ?",
                (resultSet, i) -> {
                    String id = resultSet.getString(1);
                    String text = resultSet.getString(2);
                    return new QuestionAnswer(id, text);
                }, questionId);
        QuestionWithOptions questionWithOptions = jdbcOperations.queryForObject(
                "SELECT id_question, text FROM questions WHERE id_question = ?",
                (resultSet, i) -> {
                    String idQuestion = resultSet.getString(1);
                    String text = resultSet.getString(2);
                    return new QuestionWithOptions(idQuestion, text, new ArrayList<>());
                },
                questionId);
        questionWithOptions.setAnswersList(answers);
        return questionWithOptions;
    }

    /**
     * Get correct answer by question id
     *
     * @param roomId     - room id
     * @param questionId - question id
     * @param playerId   - player id
     * @return correct answer
     * @throws QuizzesException - question exception
     */
    @Override
    public QuestionAnswer getCorrectAnswer(final String roomId, final String questionId, final String playerId) throws QuizzesException {
        try {
            String idRoom = jdbcOperations.queryForObject("SELECT id_room FROM rooms WHERE id_room = ?",
                    (result, i) -> result.getString(1), roomId);
            String idPlayer = jdbcOperations.queryForObject("SELECT id_player FROM rooms_in_player WHERE id_room = ? and id_player = ?",
                    (result, i) -> result.getString(1), roomId, playerId);
            String idQuestion = jdbcOperations.queryForObject("SELECT id_question FROM questions WHERE id_question = ?",
                    (result, i) -> result.getString(1), questionId);
            Question question = jdbcOperations.queryForObject("SELECT id_question FROM player_in_game WHERE id_player = ?",
                    (result, i) -> new Question(result.getString(1)), playerId);
            if (!idQuestion.equals(question.getQuestionId())) {
                throw new QuizzesException(QuizzesErrorCode.QUESTION_NOT_CURRENT_IN_GAME);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new QuizzesException(QuizzesErrorCode.ROOM_OR_ANSWER_OR_PLAYER_NOT_FOUND);
        }
        return jdbcOperations.queryForObject(
                "SELECT DISTINCT questions.id_correct_answer, answers.text FROM questions INNER JOIN answers ON " +
                        "questions.id_question = answers.id_question " +
                        "WHERE questions.id_question = ? and questions.id_correct_answer = answers.id_answer",
                (resultSet, i) -> {
                    String idCorrectAnswer = resultSet.getString(1);
                    String textAnswer = resultSet.getString(2);
                    return new QuestionAnswer(idCorrectAnswer, textAnswer);
                },
                questionId);
    }
}
