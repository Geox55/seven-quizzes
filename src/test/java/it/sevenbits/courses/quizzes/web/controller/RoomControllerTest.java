package it.sevenbits.courses.quizzes.web.controller;

import it.sevenbits.courses.quizzes.core.repository.QuizzesException;
import it.sevenbits.courses.quizzes.web.controller.security.UserCredentials;
import it.sevenbits.courses.quizzes.web.model.room.*;
import it.sevenbits.courses.quizzes.web.service.game.GameService;
import it.sevenbits.courses.quizzes.web.service.room.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomControllerTest {
    private GameService mockGameService;
    private RoomService mockRoomService;
    private RoomController roomController;

    @BeforeEach
    public void setup() {
        mockGameService = mock(GameService.class);
        mockRoomService = mock(RoomService.class);
        roomController = new RoomController(mockRoomService, mockGameService);
    }

    @Test
    void getRoomsTest() {
        List<RoomsResponse> mockRoomsResponse = mock(ArrayList.class);
        GetRoomsResponse mockGetRooms = mock(GetRoomsResponse.class);
        when(mockRoomService.getRooms()).thenReturn(mockGetRooms);
        when(mockGetRooms.getRoomsResponse()).thenReturn(mockRoomsResponse);
        ResponseEntity<List<RoomsResponse>> answer = roomController.getRooms();
        verify(mockRoomService, times(1)).getRooms();
        assertSame(mockRoomsResponse, answer.getBody());
    }

    @Test
    void createRoomTest() {
        CreateRoomResponse mockCreateRoomResponse = mock(CreateRoomResponse.class);
        CreateRoomRequest mockCreateRoomRequest = mock(CreateRoomRequest.class);
        UserCredentials mockUserCredentials = mock(UserCredentials.class);
        try {
            when(mockRoomService.createRoom(mockCreateRoomRequest, mockUserCredentials)).thenReturn(mockCreateRoomResponse);
            ResponseEntity<CreateRoomResponse> answer = roomController.createRoom(mockCreateRoomRequest, mockUserCredentials);
            verify(mockRoomService, times(1)).createRoom(mockCreateRoomRequest, mockUserCredentials);
            assertSame(mockCreateRoomResponse, answer.getBody());
        } catch (QuizzesException e) {
            fail();
        }
    }

    @Test
    void getRoomIdTest() {
        try {
            GetRoomResponse mockGetRoomResponse = mock(GetRoomResponse.class);
            when(mockRoomService.getRoomId("1")).thenReturn(mockGetRoomResponse);
            ResponseEntity<GetRoomResponse> answer = roomController.getRoomId("1");
            verify(mockRoomService, times(1)).getRoomId("1");
            assertSame(mockGetRoomResponse, answer.getBody());
        } catch (QuizzesException e) {
            fail();
        }
    }

}