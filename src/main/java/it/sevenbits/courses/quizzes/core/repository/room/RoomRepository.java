package it.sevenbits.courses.quizzes.core.repository.room;

import it.sevenbits.courses.quizzes.core.model.player.Player;
import it.sevenbits.courses.quizzes.core.repository.QuizzesErrorCode;
import it.sevenbits.courses.quizzes.core.repository.QuizzesException;
import it.sevenbits.courses.quizzes.web.model.room.CreateRoomResponse;
import it.sevenbits.courses.quizzes.web.model.room.GetRoomResponse;
import it.sevenbits.courses.quizzes.web.model.room.RoomsResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Room repository
 */
@Repository
public class RoomRepository implements IRoomRepository {
    private final JdbcOperations jdbcOperations;
    private static final int THIRD_COLUMN = 3;

    /**
     * Constructor
     *
     * @param jdbcOperations - interface for database
     */
    public RoomRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    /**
     * Get list of rooms
     *
     * @return list of rooms
     */
    @Override
    public List<RoomsResponse> getRooms() {
        return jdbcOperations.query("SELECT id_room, room_name FROM rooms",
                (result, i) -> new RoomsResponse(result.getString(1), result.getString(2)));
    }

    /**
     * Create room
     *
     * @param playerId - info about room
     * @param roomName - room name
     * @return - room response
     */
    @Override
    public CreateRoomResponse createRoom(final String playerId, final String roomName) throws QuizzesException {
        try {
            jdbcOperations.queryForObject("SELECT id_room FROM rooms_in_player WHERE id_player = ?",
                    (result, i) -> result.getString("id_room"), playerId);
            throw new QuizzesException(QuizzesErrorCode.PLAYER_ALREADY_EXIST);
        } catch (EmptyResultDataAccessException ignored) {
        }

        String roomId = UUID.randomUUID().toString();
        jdbcOperations.update("INSERT INTO rooms (id_room, room_name, owner) VALUES (?, ?, ?)", roomId, roomName, playerId);
        jdbcOperations.update("INSERT INTO rooms_in_player (id_room, id_player) VALUES (?, ?)", roomId, playerId);
        return new CreateRoomResponse(roomId, roomName, new Player(playerId), playerId);
    }

    /**
     * get room by id
     *
     * @param roomId - room id
     * @return - response room
     */
    @Override
    public GetRoomResponse getRoomId(final String roomId) {
        return jdbcOperations.queryForObject(
                "SELECT DISTINCT rooms_in_player.id_room, rooms_in_player.id_player, rooms.room_name, rooms.owner " +
                        "FROM rooms_in_player INNER JOIN rooms ON rooms_in_player.id_room = rooms.id_room WHERE rooms.id_room = ?",
                (result, i) -> new GetRoomResponse(result.getString(1), result.getString(THIRD_COLUMN),
                        result.getString(2), result.getString(2)), roomId);
    }

    /**
     * Player leave room
     *
     * @param playerId - info about player and room
     * @return - true if successful
     */
    @Override
    public boolean leaveRoom(final String playerId) {
        List<String> result = jdbcOperations.query("SELECT id_player FROM rooms_in_player WHERE id_player = ?",
                (res, i) -> res.getString(1), playerId);
        jdbcOperations.update("DELETE FROM rooms_in_player WHERE id_player = ?", playerId);
        return result.size() == 1;
    }
}
