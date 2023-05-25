package it.sevenbits.courses.quizzes.web.model.room;

/**
 * Leave room request
 */
public class LeaveRoomRequest {
    private String playerId;

    /**
     * constructor
     * @param playerId - player id
     */
    public LeaveRoomRequest(final String playerId) {
        this.playerId = playerId;
    }

    public void setPlayerId(final String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }
}
