package group_8.project_evnt.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import group_8.project_evnt.AddPollFragment;
import group_8.project_evnt.R;
import group_8.project_evnt.core.Database;
import group_8.project_evnt.models.Poll;
import group_8.project_evnt.models.PollAnswer;

/**
 * Created by Felix on 2017-10-18.
 */

public class EditablePollViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PollViewholder {

    private static final String ARG_ROOM_ID = "roomid";
    private static final String ARG_POLL_ID = "pollid";

    private Context context;
    private String currentRoomId;
    private Poll currentPoll;
    private ArrayList<PollAnswer> pollAnswers = new ArrayList<>();

    private RecyclerView mPollAlternativeRecycleView;
    private ImageButton menuButton;
    private Button publishButton;
    private EditText etTitle, etQuestion;


    private LinearLayoutManager mLinearLayoutManager;
    private AddPollFragment.AddAlternativeAdapter mAddAlternativeAdapter;

    public EditablePollViewHolder(View itemView, Context ctx) {
        super(itemView);
        context = ctx;

        mPollAlternativeRecycleView = itemView.findViewById(R.id.rv_alternatives);

        menuButton = itemView.findViewById(R.id.menu_button);
        menuButton.setOnClickListener(this);

        publishButton = itemView.findViewById(R.id.publish_button);
        publishButton.setOnClickListener(this);

        etTitle = itemView.findViewById(R.id.text_input_title);
        etQuestion = itemView.findViewById(R.id.text_input_question);


    }

    public void bindPoll(String roomId, Poll poll){
        currentRoomId = roomId;
        currentPoll = poll;

        etTitle.setText(poll.getTitle());
        etQuestion.setText(poll.getQuestion());
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.menu_button:
                showPopup(view);
                break;

            case R.id.publish_button:
                publishPoll();
                break;
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_poll_card, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_item_edit:
                        break;
                    case R.id.menu_item_delete:
                        deletePoll();
                }
                return true;
            }
        });

        popup.show();
    }

    private void deletePoll(){
        Database.getInstance().deletePoll(currentRoomId, currentPoll.getKey());
    }

    private void publishPoll(){
        Poll updated = new Poll(etTitle.getText().toString(), etQuestion.getText().toString(), currentPoll.getCreator(), currentPoll.getPollAnwsers(), true);
        Database.getInstance().updatePoll(currentRoomId, currentPoll.getKey(), updated);
    }
}
