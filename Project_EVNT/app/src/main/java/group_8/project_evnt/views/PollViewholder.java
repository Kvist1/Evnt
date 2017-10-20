package group_8.project_evnt.views;

import group_8.project_evnt.models.Poll;

/**
 * Created by Felix on 2017-10-18.
 */

public interface PollViewholder {

    public void bindPoll(String currentRoomId, Poll poll);
}
