package it.sevenbits.courses.quizzes.core.model.game;

/**
 * Status of game
 */
public enum StatusOfGame {
    /**
     * ready to start
     */
    READY_TO_START("READY_TO_START"),
    /**
     * In progress
     */
    IN_PROGRESS("IN_PROGRESS"),
    /**
     * Finished
     */
    FINISHED("FINISHED");
    private final String status;

    /**
     * Constructor
     * @param string - describe
     */
    StatusOfGame(final String string) {
        status = string;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Status of game by string status
     * @param status - string status
     * @return - status of game
     */
    public static StatusOfGame fromString(final String status) {
        for (StatusOfGame statusOfGame : StatusOfGame.values()) {
            if (statusOfGame.status.equalsIgnoreCase(status)) {
                return statusOfGame;
            }
        }
        return null;
    }
}
