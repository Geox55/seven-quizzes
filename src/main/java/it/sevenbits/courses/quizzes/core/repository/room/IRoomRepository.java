package it.sevenbits.courses.quizzes.core.repository.room;

import it.sevenbits.courses.quizzes.core.repository.QuizzesException;
import it.sevenbits.courses.quizzes.web.model.room.CreateRoomResponse;
import it.sevenbits.courses.quizzes.web.model.room.RoomsResponse;
import it.sevenbits.courses.quizzes.web.model.room.GetRoomResponse;

import java.util.List;


/**
 * Interface room repository
 */
public interface IRoomRepository {
    /**
     * Get rooms
     * @return list rooms response
     */
    List<RoomsResponse> getRooms();

    /**
     * Create room
     * @param playerId - request
     * @param roomName - room name
     * @return - room response
     * @throws QuizzesException - repository exception
     */
    CreateRoomResponse createRoom(String playerId, String roomName) throws QuizzesException;

    /**
     * Get room by id
     * @param roomId - room id
     * @return - Room by id
     */
    GetRoomResponse getRoomId(String roomId);

    /**
     * Leave player from room
     * @param playerId - player info
     * @return - true if successful
     */
    boolean leaveRoom(String playerId);
}

