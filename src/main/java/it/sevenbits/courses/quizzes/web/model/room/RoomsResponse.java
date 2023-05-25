package it.sevenbits.courses.quizzes.web.model.room;

/**
 * rooms response
 */
public class RoomsResponse {
    private String roomId;
    private String roomName;

    /**
     * full constructor
     * @param roomId - room id
     * @param nameRoom - name room
     */
    public RoomsResponse(final String roomId, final String nameRoom) {
        this.roomId = roomId;
        this.roomName = nameRoom;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(final String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(final String roomName) {
        this.roomName = roomName;
    }
}
