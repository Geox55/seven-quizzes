package it.sevenbits.courses.quizzes.web.service.room;

import it.sevenbits.courses.quizzes.core.model.player.Player;
import it.sevenbits.courses.quizzes.core.repository.QuizzesException;
import it.sevenbits.courses.quizzes.core.repository.room.RoomRepository;
import it.sevenbits.courses.quizzes.web.controller.security.UserCredentials;
import it.sevenbits.courses.quizzes.web.model.room.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomServiceTest {
    private RoomService roomService;
    private RoomRepository mockRoomRepository;

    @BeforeEach
    public void setup() {
        mockRoomRepository = mock(RoomRepository.class);
        roomService = new RoomService(mockRoomRepository);
    }

    @Test
    void getRooms() {
        List<RoomsResponse> getRoomResponse = mock(List.class);
        GetRoomsResponse mockGetRoomResponse = mock(GetRoomsResponse.class);
        when(mockGetRoomResponse.getRoomsResponse()).thenReturn(getRoomResponse);
        when(mockRoomRepository.getRooms()).thenReturn(getRoomResponse);

        GetRoomsResponse answer = roomService.getRooms();

        verify(mockRoomRepository, times(1)).getRooms();
        assertSame(new GetRoomsResponse(getRoomResponse).getRoomsResponse(), answer.getRoomsResponse());
    }

    @Test
    void createRoom() {
        String playerId = "1";
        String roomName = "2";
        CreateRoomRequest mockCreateRoomRequest = mock(CreateRoomRequest.class);
        CreateRoomResponse mockCreateRoomResponse = mock(CreateRoomResponse.class);
        UserCredentials mockUserCredentials = mock(UserCredentials.class);
        try {
            when(mockUserCredentials.getPlayerId()).thenReturn(playerId);
            when(mockCreateRoomRequest.getRoomName()).thenReturn(roomName);
            when(mockRoomRepository.createRoom(playerId, roomName)).thenReturn(mockCreateRoomResponse);
            CreateRoomResponse answer = roomService.createRoom(mockCreateRoomRequest, mockUserCredentials);
            verify(mockRoomRepository, times(1)).createRoom(playerId, roomName);
            assertSame(mockCreateRoomResponse, answer);
        } catch (QuizzesException e) {
            fail();
        }
    }

    @Test
    void getRoomId() {
        GetRoomResponse mockGetRoomResponse = mock(GetRoomResponse.class);
        List<Player> mockList = mock(List.class);
        when(mockRoomRepository.getRoomId("1")).thenReturn(mockGetRoomResponse);
        when(mockGetRoomResponse.getRoomId()).thenReturn("1");
        when(mockGetRoomResponse.getRoomName()).thenReturn("1");
        when(mockGetRoomResponse.getPlayers()).thenReturn(mockList);

        try {
            GetRoomResponse answer = roomService.getRoomId("1");
            verify(mockRoomRepository, times(1)).getRoomId("1");
            assertSame(mockGetRoomResponse, answer);
        } catch (QuizzesException e) {
            fail();
        }
    }
}