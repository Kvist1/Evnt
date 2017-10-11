package group_8.project_evnt.models;

import java.security.Timestamp;

/**
 * Created by Felix on 2017-10-09.
 */

public class ChatMessage {

    private String userId;
    private String message;
    private long time;

    public ChatMessage(String usrId, String msg){
        userId = usrId;
        message = msg;
        time = System.currentTimeMillis();
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

}
