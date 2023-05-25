package it.sevenbits.courses.quizzes.web.model.room;

import it.sevenbits.courses.quizzes.core.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * get room response
 */
public class GetRoomResponse {
    private final String roomId;
    private final String roomName;
    private final List<Player> players;
    private final String ownerId;

    /**
     * full constructor by player id
     *
     * @param roomId   - room id
     * @param roomName - room name
     * @param playerId - player id
     * @param ownerId - owner id
     */
    public GetRoomResponse(final String roomId, final String roomName, final String playerId, final String ownerId) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.players = new ArrayList<>();
        players.add(new Player(playerId));
        this.ownerId = ownerId;
    }

    /**
     * full constructor
     *
     * @param roomId   - room id
     * @param roomName - room name
     * @param players  - players
     * @param ownerId  - owner id
     */
    public GetRoomResponse(final String roomId, final String roomName, final List<Player> players, final String ownerId) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.players = players;
        this.ownerId = ownerId;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetRoomResponse that = (GetRoomResponse) o;
        return Objects.equals(roomId, that.roomId) && Objects.equals(roomName, that.roomName) &&
                Objects.equals(players, that.players) && Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, roomName, players, ownerId);
    }
}
