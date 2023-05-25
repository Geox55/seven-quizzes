package it.sevenbits.courses.quizzes.core.repository;

/**
 * Quizzes Error Code
 */
public enum QuizzesErrorCode {
    /**
     * Player already exist
     */
    PLAYER_ALREADY_EXIST("User with provided id in another room, need to leave first"),
    /**
     * Room by id not found
     */
    ROOM_BY_ID_NOT_FOUND("Room with requested id was not found"),
    /**
     * Player not found
     */
    PLAYER_NOT_FOUND("Player not found"),
    /**
     * Invalid input
     */
    INVALID_INPUT("Invalid input, object invalid"),
    /**
     * Player in room not found
     */
    PLAYER_IN_ROOM_NOT_FOUND("User with provided id not in the room"),
    /**
     * Game is running
     */
    GAME_IS_RUNNING("The game in this room is running"),
    /**
     * Room or question not found
     */
    ROOM_OR_QUESTION_NOT_FOUND("Room or question with requested id was not found in the game"),
    /**
     * Room or answer or player not found
     */
    ROOM_OR_ANSWER_OR_PLAYER_NOT_FOUND("Room, answer, player or question with requested id was not found"),
    /**
     * Question not current in game
     */
    QUESTION_NOT_CURRENT_IN_GAME("Question with provided id is not current question for the player"), GAME_IS_FINISHED("Game is finished");

    /**
     * describe error
     */
    private final String error;

    /**
     * Constructor
     * @param error - describe error
     */
    QuizzesErrorCode(final String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
