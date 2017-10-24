package group_8.project_evnt.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kittipon on 10/16/17.
 */

public class PollAnswer {
    private String answer;
    private HashMap<String, Boolean> voters;

    public PollAnswer() {
    }

    public PollAnswer(String answer) {
        this.answer = answer;
        this.voters = new HashMap<>();
    }

    public PollAnswer(String answer, HashMap<String, Boolean> voters) {
        this.answer = answer;
        this.voters = voters;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public HashMap<String, Boolean> getVoters() {
        return voters;
    }

    public void setVoters(HashMap<String, Boolean> voters) {
        this.voters = voters;
    }

    public int countVoters() {
        int count = 0;
        for (Boolean isVote : voters.values()) {
            if(isVote) count++;
        }
        return count;
    }
}
