package group_8.project_evnt.models;

/**
 * Created by Kvist1 on 2017-10-16.
 */

public class PollAlternative {

    private String pollId;
    private String text;

    public PollAlternative(){

    }

    public String getPollId() { return pollId; }

    public void setPollId(String pollId) { this.pollId = pollId; }

    public String getAlternative() { return text; }

    public void setText(String text) {
        this.text = text;
    }

}
