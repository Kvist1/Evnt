package group_8.project_evnt.models;

/**
 * Created by kittipon on 10/16/17.
 */

public class Room {
    public String roomId;
    public String roomCode;
    public long time;
    public String creator;

    public Room(){
        // empty constructor for firebase
    }

    public Room(String roomId, String roomCode, String creator) {
        this.roomId = roomId;
        this.roomCode = roomCode;
        this.creator = creator;
        this.time = System.currentTimeMillis();
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
