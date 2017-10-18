package group_8.project_evnt.models;

import java.util.ArrayList;

/**
 * Created by kittipon on 10/16/17.
 */

public class PollAnswer {
    private String answer;
    private ArrayList<String> voters;

    public PollAnswer() {
    }

    public PollAnswer(String answer) {
        this.answer = answer;
        this.voters = new ArrayList<String>();
    }

    public PollAnswer(String answer, ArrayList<String> voters) {
        this.answer = answer;
        this.voters = voters;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public ArrayList<String> getVoters() {
        return voters;
    }

    public void setVoters(ArrayList<String> voters) {
        this.voters = voters;
    }

    public void addVoter(String userId) {
        this.voters.add(userId);
    }

    public boolean removeVoter(String userId) {
        int index = this.voters.indexOf(userId);
        if(index != -1) {
            this.voters.remove(index);
            return true;
        }
        return false;
    }
}
