package group_8.project_evnt.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

    private RecyclerView mPollAlternativeRecycleView;
    private ImageButton menuButton;
    private Button publishButton;
    private EditText etTitle, etQuestion;


    private LinearLayoutManager mLinearLayoutManager;
    private AddAlternativeAdapter mAddAlternativeAdapter;

    public EditablePollViewHolder(View itemView, Context ctx) {
        super(itemView);
        context = ctx;
        // Setup views
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

        if (currentPoll.getPollAnwsers() == null){
            currentPoll.setPollAnwsers(new ArrayList<PollAnswer>());
        }

        // Create empty alternative in the bottom;
        currentPoll.getPollAnwsers().add(new PollAnswer());

        mAddAlternativeAdapter = new AddAlternativeAdapter(context, currentPoll.getPollAnwsers());
        mPollAlternativeRecycleView.setAdapter(mAddAlternativeAdapter);
        mLinearLayoutManager = new LinearLayoutManager(context);
        mPollAlternativeRecycleView.setLayoutManager(mLinearLayoutManager);

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


    /**
     * POLL ALTERNATIVE ADAPTER
     **/

    public class AddAlternativeAdapter extends
            RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<PollAnswer> mPollAnswers;
        private Context mContext;

        public AddAlternativeAdapter(Context context, ArrayList<PollAnswer> pollAlternatives) {
            mPollAnswers = pollAlternatives;
            mContext = context;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements TextWatcher {
            public TextView text_plus_sign;
            public EditText et_alternative;
            private Context context;

            public ViewHolder(Context context, View itemView) {
                super(itemView);
                this.context = context;

                text_plus_sign = (TextView) itemView.findViewById(R.id.plus_sign);
                et_alternative = (EditText) itemView.findViewById(R.id.add_alternative);
                et_alternative.addTextChangedListener(this);
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                int position = -1;
                if (et_alternative.getTag() != null)
                    position = (int) et_alternative.getTag();

                if (position == mPollAnswers.size()-1 &&
                        !et_alternative.getText().toString().equals("")) {

                    mPollAnswers.get(position).setAnswer(et_alternative.getText().toString());
                    mPollAnswers.add(new PollAnswer());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View yourMessageView = inflater.inflate(R.layout.fragment_add_alt, parent, false);

            // Return a new holder instance
            AddAlternativeAdapter.ViewHolder viewHolder = new AddAlternativeAdapter.ViewHolder(context, yourMessageView);
            return viewHolder;
        }

        // Involves populating data into the item through holder
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            // Get the data model based on position
            PollAnswer alt = mPollAnswers.get(position);
            AddAlternativeAdapter.ViewHolder alternativeView = (AddAlternativeAdapter.ViewHolder) viewHolder;

            // Set item views based on your views and data model
            EditText alternative = alternativeView.et_alternative;
            alternative.setText(alt.getAnswer());
            alternative.setTag(position);
        }

        @Override
        public int getItemCount() {
            return mPollAnswers.size();
        }
    }
}
