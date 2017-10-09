package group_8.project_evnt.models;

import java.security.Timestamp;

/**
 * Created by Felix on 2017-10-09.
 */

public class ChatMessage {

    private String author;
    private String message;
    private Timestamp time;

    public ChatMessage(String auth, String msg){
        author = auth;
        message = msg;
        time = null;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

}
