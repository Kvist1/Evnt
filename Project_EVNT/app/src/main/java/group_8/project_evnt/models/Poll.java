package group_8.project_evnt.models;

import java.util.ArrayList;

/**
 * Created by kittipon on 10/16/17.
 */

public class Poll {
    private String title;
    private String question;
    private boolean isLive;
    private boolean isEnd;
    private String creator;
    private ArrayList<PollAnswer> pollAnwsers;

    public Poll() {
    }

    public Poll(String title, String question, String creator, ArrayList<PollAnswer> pollAnwsers, boolean isLive) {
        this.title = title;
        this.question = question;
        this.creator = creator;
        this.pollAnwsers = pollAnwsers;
        this.isLive = isLive;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<PollAnswer> getPollAnwsers() {
        return pollAnwsers;
    }

    public void setPollAnwsers(ArrayList<PollAnswer> pollAnwsers) {
        this.pollAnwsers = pollAnwsers;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
