package group_8.project_evnt.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import group_8.project_evnt.models.Poll;

/**
 * Created by Felix on 2017-10-18.
 */

public class VotePollViewHolder extends RecyclerView.ViewHolder implements PollViewholder {

    public VotePollViewHolder(View itemView){
        super(itemView);
    }

    @Override
    public void bindPoll(String currentRoomId, Poll poll) {

    }
}
