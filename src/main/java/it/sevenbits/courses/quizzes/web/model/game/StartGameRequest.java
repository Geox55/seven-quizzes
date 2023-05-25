package it.sevenbits.courses.quizzes.web.model.game;

/**
 * Start game request
 */
public class StartGameRequest {
    private String playerId;

    /**
     * Empty constructor
     */
    public StartGameRequest() {
    }


    /**
     * Start game request
     *
     * @param playerId - player ud
     */
    public StartGameRequest(final String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(final String playerId) {
        this.playerId = playerId;
    }
}
