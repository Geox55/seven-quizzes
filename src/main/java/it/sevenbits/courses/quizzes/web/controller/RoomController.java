package it.sevenbits.courses.quizzes.web.controller;

import it.sevenbits.courses.quizzes.core.repository.QuizzesErrorCode;
import it.sevenbits.courses.quizzes.core.repository.QuizzesException;
import it.sevenbits.courses.quizzes.web.controller.security.AuthRoleRequired;
import it.sevenbits.courses.quizzes.web.controller.security.UserCredentials;
import it.sevenbits.courses.quizzes.web.model.room.CreateRoomRequest;
import it.sevenbits.courses.quizzes.web.model.room.CreateRoomResponse;
import it.sevenbits.courses.quizzes.web.model.room.GetRoomResponse;
import it.sevenbits.courses.quizzes.web.model.room.RoomsResponse;
import it.sevenbits.courses.quizzes.web.service.game.IGameService;
import it.sevenbits.courses.quizzes.web.service.room.IRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Room controller
 */
@RestController
public class RoomController {
    private final IRoomService roomService;
    private final IGameService gameService;

    /**
     * constructor
     *
     * @param roomService - room service
     * @param gameService - game service
     */
    public RoomController(final IRoomService roomService, final IGameService gameService) {
        this.roomService = roomService;
        this.gameService = gameService;
    }

    /**
     * get rooms
     *
     * @return - response list rooms
     */
    @CrossOrigin("http://localhost:3000")
    @GetMapping("rooms")
    @AuthRoleRequired("USER")
    public ResponseEntity<List<RoomsResponse>> getRooms() {
        return ResponseEntity.ok().body(roomService.getRooms().getRoomsResponse());
    }

    /**
     * create room
     *
     * @param createRoomRequest - info about room
     * @param userCredentials - user credentials
     * @return - response create room
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("rooms")
    @AuthRoleRequired("USER")
    public ResponseEntity<CreateRoomResponse> createRoom(@RequestBody final CreateRoomRequest createRoomRequest,
                                                         final UserCredentials userCredentials) {
        try {
            CreateRoomResponse createRoomResponse = roomService.createRoom(createRoomRequest, userCredentials);
            gameService.createGame(createRoomResponse.getRoomId(), userCredentials.getPlayerId());
            return ResponseEntity.ok().body(createRoomResponse);
        } catch (QuizzesException e) {
            if (e.getErrorCode() == QuizzesErrorCode.PLAYER_ALREADY_EXIST) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * get room by id
     *
     * @param roomId - room id
     * @return - response room
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("rooms/{roomId}")
    @AuthRoleRequired("USER")
    public ResponseEntity<GetRoomResponse> getRoomId(@PathVariable(value = "roomId") final String roomId) {
        try {
            return ResponseEntity.ok().body(roomService.getRoomId(roomId));
        } catch (QuizzesException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * leave player from room
     *
     * @param userCredentials - info about player and room
     * @return - response status
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("rooms/leave")
    @AuthRoleRequired("USER")
    public ResponseEntity<?> leaveRoom(final UserCredentials userCredentials) {
        try {
            roomService.leaveRoom(userCredentials);
            return ResponseEntity.ok().build();
        } catch (QuizzesException e) {
            if (e.getErrorCode() == QuizzesErrorCode.PLAYER_NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else if (e.getErrorCode() == QuizzesErrorCode.PLAYER_IN_ROOM_NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
