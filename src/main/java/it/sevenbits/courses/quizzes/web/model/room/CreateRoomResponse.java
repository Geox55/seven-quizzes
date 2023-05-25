package it.sevenbits.courses.quizzes.web.model.room;

import it.sevenbits.courses.quizzes.core.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Create room response
 */
public class CreateRoomResponse {
    private String roomId;
    private String roomName;
    private List<Player> players;
    private String ownerId;

    /**
     * Empty constructor
     */
    public CreateRoomResponse() {
    }

    /**
     * full constructor
     *
     * @param roomId   - room id
     * @param roomName - room name
     * @param players  - players
     * @param ownerId - ownerId
     */
    public CreateRoomResponse(final String roomId, final String roomName, final List<Player> players, final String ownerId) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.players = players;
        this.ownerId = ownerId;
    }

    /**
     * constructor by player
     *
     * @param roomId   - room id
     * @param roomName - room name
     * @param player   - player
     * @param ownerId  - onwer id
     */
    public CreateRoomResponse(final String roomId, final String roomName, final Player player, final String ownerId) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.players = new ArrayList<>();
        this.players.add(player);
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

    public void setRoomId(final String roomId) {
        this.roomId = roomId;
    }

    public void setRoomName(final String roomName) {
        this.roomName = roomName;
    }

    public void setPlayers(final List<Player> players) {
        this.players = players;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(final String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateRoomResponse that = (CreateRoomResponse) o;
        return Objects.equals(roomId, that.roomId) && Objects.equals(roomName, that.roomName)
                && Objects.equals(players, that.players) && Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, roomName, players, ownerId);
    }
}
