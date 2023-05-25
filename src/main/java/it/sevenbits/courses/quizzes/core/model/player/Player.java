package it.sevenbits.courses.quizzes.core.model.player;

import java.util.Objects;

/**
 * Player
 */
public class Player {
    private String playerId;

    /**
     * Empty constructor
     */
    public Player() {
    }

    /**
     * Constructor
     * @param playerId - player id
     */
    public Player(final String playerId) {
        this.playerId = playerId;
    }


    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(final String playerId) {
        this.playerId = playerId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return Objects.equals(playerId, player.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId);
    }
}
