package group_8.project_evnt.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import group_8.project_evnt.R;
import group_8.project_evnt.models.Poll;
import group_8.project_evnt.models.PollAnswer;

/**
 * Created by Felix on 2017-10-18.
 */

public class VotePollViewHolder extends RecyclerView.ViewHolder implements PollViewholder {

    private Context context;
    private String currentRoomId;
    private Poll currentPoll;

    private TextView pollTitle;
    private TextView pollQuestion;
    private RecyclerView answerList;


    public VotePollViewHolder(View itemView, Context ctx){
        super(itemView);
        context = ctx;

        pollTitle = (TextView) itemView.findViewById(R.id.tv_poll_title);
        pollQuestion = (TextView) itemView.findViewById(R.id.tv_poll_question);
        answerList = (RecyclerView) itemView.findViewById(R.id.rv_poll_answer_list);
    }

    @Override
    public void bindPoll(String roomId, Poll poll) {
        currentRoomId = roomId;
        currentPoll = poll;

        pollTitle.setText(currentPoll.getTitle());
        pollQuestion.setText(currentPoll.getQuestion());

        if (currentPoll.getPollAnwsers() == null){
            currentPoll.setPollAnwsers(new ArrayList<PollAnswer>());
        }

        VoteAlternativeAdapter adapter = new VoteAlternativeAdapter(context, currentPoll.getPollAnwsers());
        answerList.setAdapter(adapter);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        answerList.setLayoutManager(lm);



    }


    public class VoteAlternativeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<PollAnswer> mPollAnswers;
        private Context mContext;

        public VoteAlternativeAdapter(Context context, ArrayList<PollAnswer> pollAlternatives) {
            mPollAnswers = pollAlternatives;
            mContext = context;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private Context context;

            public ViewHolder(Context context, View itemView) {
                super(itemView);
                this.context = context;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View yourMessageView = inflater.inflate(R.layout.poll_list_item, parent, false);

            // Return a new holder instance
            return new VoteAlternativeAdapter.ViewHolder(context, yourMessageView);
        }

        // Involves populating data into the item through holder
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            PollAnswer alt = mPollAnswers.get(position);

            // SET ALL THE TEXTS
        }

        @Override
        public int getItemCount() {
            return mPollAnswers.size();
        }
    }
}
