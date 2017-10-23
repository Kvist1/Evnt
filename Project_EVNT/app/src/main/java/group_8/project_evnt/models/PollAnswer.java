package group_8.project_evnt.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kittipon on 10/16/17.
 */

public class PollAnswer {
    private String answer;
    private HashMap<String, Object> voters;

    public PollAnswer() {
    }

    public PollAnswer(String answer) {
        this.answer = answer;
        this.voters = new HashMap<>();
    }

    public PollAnswer(String answer, HashMap<String, Object> voters) {
        this.answer = answer;
        this.voters = voters;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public HashMap<String, Object> getVoters() {
        return voters;
    }

    public void setVoters(HashMap<String, Object> voters) {
        this.voters = voters;
    }

    public void addVoter(String userId) {
        this.voters.put(userId, true);
    }

    public boolean removeVoter(String userId) {
        this.voters.remove(userId);
        return false;
    }
}
