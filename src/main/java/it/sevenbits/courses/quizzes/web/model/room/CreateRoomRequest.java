package it.sevenbits.courses.quizzes.web.model.room;


/**
 * Create room request
 */
public class CreateRoomRequest {
    private String roomName;

    /**
     * full constructor
     * @param roomName - room name
     */
    public CreateRoomRequest(final String roomName) {
        this.roomName = roomName;
    }

    /**
     * Empty constructor
      */
    public CreateRoomRequest() {

    }

    public void setRoomName(final String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }
}

