package it.sevenbits.courses.quizzes.web.service.room;

import it.sevenbits.courses.quizzes.core.repository.QuizzesException;
import it.sevenbits.courses.quizzes.web.controller.security.UserCredentials;
import it.sevenbits.courses.quizzes.web.model.room.CreateRoomRequest;
import it.sevenbits.courses.quizzes.web.model.room.CreateRoomResponse;
import it.sevenbits.courses.quizzes.web.model.room.GetRoomResponse;
import it.sevenbits.courses.quizzes.web.model.room.GetRoomsResponse;


/**
 * Interface room service
 */
public interface IRoomService {
    /**
     * Get rooms
     *
     * @return - rooms
     */
    GetRoomsResponse getRooms();

    /**
     * Create room
     *
     * @param createRoomRequest - room request
     * @param userCredentials - user credentials
     * @return room response
     * @throws QuizzesException - repository exception
     */
    CreateRoomResponse createRoom(CreateRoomRequest createRoomRequest, UserCredentials userCredentials) throws QuizzesException;

    /**
     * Get room id
     *
     * @param roomId - room id
     * @return - get room response
     * @throws QuizzesException - repository exception
     */
    GetRoomResponse getRoomId(String roomId) throws QuizzesException;

    /**
     * Leave room
     * @param userCredentials - user credentials
     * @throws QuizzesException - repository exception
     */
    void leaveRoom(UserCredentials userCredentials) throws QuizzesException;
}

