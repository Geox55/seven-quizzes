package it.sevenbits.courses.quizzes.config;

import it.sevenbits.courses.quizzes.core.repository.game.GameRepository;
import it.sevenbits.courses.quizzes.core.repository.question.QuestionRepository;
import it.sevenbits.courses.quizzes.core.repository.room.RoomRepository;
import it.sevenbits.courses.quizzes.core.repository.security.BCryptPasswordEncoder;
import it.sevenbits.courses.quizzes.core.repository.security.PasswordEncoder;
import it.sevenbits.courses.quizzes.core.repository.user.UserRepository;
import it.sevenbits.courses.quizzes.web.controller.security.JsonWebTokenService;
import it.sevenbits.courses.quizzes.web.controller.security.JwtSettings;
import it.sevenbits.courses.quizzes.web.controller.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;

/**
 * Repository config
 */
@Configuration
public class RepositoryConfig {
    /**
     * config question repository
     * @param jdbcOperations - interface for database
     * @return - Question repository
     */
    @Bean
    public QuestionRepository configQuestionRepository(
            @Qualifier("quizzesJdbcOperations") final
            JdbcOperations jdbcOperations
    ) {
        return new QuestionRepository(jdbcOperations);
    }

    /**
     * config game repository
     * @param jdbcOperations - interface for database
     * @return - Game repository
     */
    @Bean
    public GameRepository configGameRepository(
            @Qualifier("quizzesJdbcOperations") final
            JdbcOperations jdbcOperations
    ) {
        return new GameRepository(jdbcOperations);
    }

    /**
     * Config room repository
     * @param jdbcOperations - interface for database
     * @return - room repository
     */
    @Bean
    public RoomRepository configRoomRepository(
            @Qualifier("quizzesJdbcOperations") final
            JdbcOperations jdbcOperations
    ) {
        return new RoomRepository(jdbcOperations);
    }

    /**
     * repository
     * @param jdbcOperations - operations
     * @return user repository
     */
    @Bean
    public UserRepository userQuizzesRepository(
            @Qualifier("quizzesJdbcOperations")
            final JdbcOperations jdbcOperations
    ) {
        return new UserRepository(jdbcOperations);
    }

    /**
     * password encoder
     * @return password
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * jwt token service
     * @param settings - settings
     * @return token
     */
    @Bean
    public JwtTokenService jwtTokenService(final JwtSettings settings) {
        return new JsonWebTokenService(settings);
    }
}