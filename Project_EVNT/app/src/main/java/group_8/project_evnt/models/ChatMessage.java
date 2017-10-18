package group_8.project_evnt.models;

import java.security.Timestamp;

/**
 * Created by Felix on 2017-10-09.
 */

public class ChatMessage {

    private String userId;
    private String message;
    private long time;
    private boolean isCreator;

    public ChatMessage(){
        // empty constructor for firebase
    }

    public ChatMessage(String usrId, String msg, boolean creator){
        userId = usrId;
        message = msg;
        time = System.currentTimeMillis();
        isCreator = creator;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isCreator() {
        return isCreator;
    }

    public void setCreator(boolean creator) {
        isCreator = creator;
    }

}
