package it.sevenbits.courses.quizzes.core.model.player;

/**
 * Player score
 */
public class PlayerScore {
    private String playerId;
    private String name;
    private int score;

    /**
     * Constructor
     *
     * @param playerId - player id
     * @param name name
     * @param score    - score
     */
    public PlayerScore(final String playerId, final String name, final int score) {
        this.playerId = playerId;
        this.name = name;
        this.score = score;
    }

    /**
     * Empty constructor
     */
    public PlayerScore() {
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(final String playerId) {
        this.playerId = playerId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(final int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
