package it.sevenbits.courses.quizzes.web.service.room;

import it.sevenbits.courses.quizzes.core.repository.QuizzesErrorCode;
import it.sevenbits.courses.quizzes.core.repository.QuizzesException;
import it.sevenbits.courses.quizzes.core.repository.room.IRoomRepository;
import it.sevenbits.courses.quizzes.web.controller.security.UserCredentials;
import it.sevenbits.courses.quizzes.web.model.room.CreateRoomRequest;
import it.sevenbits.courses.quizzes.web.model.room.CreateRoomResponse;
import it.sevenbits.courses.quizzes.web.model.room.GetRoomResponse;
import it.sevenbits.courses.quizzes.web.model.room.GetRoomsResponse;
import org.springframework.stereotype.Service;

/**
 * Room service
 */
@Service
public class RoomService implements IRoomService {
    private final IRoomRepository roomRepository;

    /**
     * Constructor
     *
     * @param roomRepository - room repository
     */
    public RoomService(final IRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * get rooms
     *
     * @return - rooms response
     */
    @Override
    public GetRoomsResponse getRooms() {
        return new GetRoomsResponse(roomRepository.getRooms());
    }

    /**
     * create room
     *
     * @param createRoomRequest - room request
     * @param userCredentials - user credentials
     * @return room response
     */
    @Override
    public CreateRoomResponse createRoom(final CreateRoomRequest createRoomRequest,
                                         final UserCredentials userCredentials) throws QuizzesException {
        if (createRoomRequest == null || userCredentials.getPlayerId() == null || createRoomRequest.getRoomName() == null) {
            throw new QuizzesException(QuizzesErrorCode.INVALID_INPUT);
        }
        return roomRepository.createRoom(userCredentials.getPlayerId(), createRoomRequest.getRoomName());
    }

    /**
     * get room by id
     *
     * @param roomId - room id
     * @return room response
     */
    @Override
    public GetRoomResponse getRoomId(final String roomId) throws QuizzesException {
        GetRoomResponse roomResponse = roomRepository.getRoomId(roomId);
        if (roomResponse == null) {
            throw new QuizzesException(QuizzesErrorCode.ROOM_BY_ID_NOT_FOUND);
        }
        return roomResponse;
    }

    /**
     * leave room
     * @param userCredentials - user credentials
     */
    @Override
    public void leaveRoom(final UserCredentials userCredentials) throws QuizzesException {
        if (userCredentials == null || userCredentials.getPlayerId() == null) {
            throw new QuizzesException(QuizzesErrorCode.INVALID_INPUT);
        } else if ("".equals(userCredentials.getPlayerId())) {
            throw new QuizzesException(QuizzesErrorCode.PLAYER_NOT_FOUND);
        } else if (!roomRepository.leaveRoom(userCredentials.getPlayerId())) {
            throw new QuizzesException(QuizzesErrorCode.PLAYER_IN_ROOM_NOT_FOUND);
        }
    }
}
