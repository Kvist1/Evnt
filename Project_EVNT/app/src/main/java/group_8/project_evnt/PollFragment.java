package group_8.project_evnt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import group_8.project_evnt.core.Database;
import group_8.project_evnt.models.ChatMessage;
import group_8.project_evnt.models.Poll;
import group_8.project_evnt.models.PollAnswer;
import group_8.project_evnt.models.Room;
import group_8.project_evnt.utils.AppUtils;

import static android.R.attr.fragment;
import static android.R.attr.maxWidth;
import static android.R.attr.width;
import static group_8.project_evnt.R.id.container;
import static group_8.project_evnt.R.id.parent;

public class PollFragment extends Fragment {
    private String currentRoomId;
    private ArrayList<Poll> polls = new ArrayList<Poll>();

    private static final String ARG_ROOM_ID = "roomid";
    private static final String ARG_POLL_ID = "pollid";

    private RecyclerView mPollListRecycleView;

    private LinearLayoutManager mLinearLayoutManager;
    private PollFragment.PollListAdapter mPollListAdapter;

    private FragmentActivity myContext;

    public boolean isCreator;
    FloatingActionButton newPollFab;

    public PollFragment () {
    }

    public static PollFragment newInstance(String roomId) {
        PollFragment fragment = new PollFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROOM_ID, roomId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            currentRoomId = getArguments().getString(ARG_ROOM_ID);
        } else {
            return;
        }

        // get room info for check creator
        final Database db = Database.getInstance();
        final String userId = AppUtils.getDeviceId(getContext());
        db.findRoomById(currentRoomId, new Database.CreateRoomCallbackInterface() {
            @Override
            public void onRoomRetrieved(Room room) {
                if (room != null) {
                    isCreator = room.getCreator().equals(userId);

                    if(!isCreator) {
                        newPollFab.setVisibility(View.GONE);
                    }
                }
            }
        });


        DatabaseReference poll = Database.getInstance().poll(currentRoomId);

        poll.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Poll poll = dataSnapshot.getValue(Poll.class);
                poll.setKey(dataSnapshot.getKey());
                if (poll.isLive()){
                    polls.add(poll);
                    mPollListAdapter.notifyItemInserted(polls.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Poll poll = dataSnapshot.getValue(Poll.class);
                poll.setKey(dataSnapshot.getKey());

                // TODO, replace with proper polls.indexOf() instead of this hacky solution
                int index = -1;
                for (int i = 0; i < polls.size(); i++){
                    if (polls.get(i).getKey().equals(poll.getKey())){
                        index = i;
                        break;

                    }
                }

                if (index > -1){
                    polls.set(index, poll);
                    mPollListAdapter.notifyItemChanged(index);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Poll poll = dataSnapshot.getValue(Poll.class);
                poll.setKey(dataSnapshot.getKey());

                // TODO, replace with proper polls.indexOf() instead of this hacky solution
                int index = -1;
                for (int i = 0; i < polls.size(); i++){
                    if (polls.get(i).getKey().equals(poll.getKey())){
                        index = i;
                        break;

                    }
                }

                if (index > -1){
                    polls.remove(index);
                    mPollListAdapter.notifyItemRemoved(index);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showDialog(String pollId) {
        String fragmentTag = "ADD_POLL";
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        AddPollFragment fragment = AddPollFragment.newInstance(currentRoomId, pollId);

        // Make sure the current transaction finishes first
        fragmentManager.executePendingTransactions();

        // If there is no fragment yet with this tag...
        if (fragmentManager.findFragmentByTag(fragmentTag) == null) {
            // The device is smaller, so show the fragment fullscreen
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction.add(android.R.id.content, fragment, fragmentTag)
                    .addToBackStack(null).commit();
//        }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_poll, container, false);

        // findViewById stuff
        // Inflate the layout for this fragment
        mPollListRecycleView = rootView.findViewById(R.id.rv_poll_list);

        newPollFab = (FloatingActionButton) rootView.findViewById(R.id.new_poll_fab);
        final LinearLayout l1 = (LinearLayout) rootView.findViewById(R.id.linear_layout_container);

        newPollFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(null);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Setup adapter
        // Create adapter passing in the sample user data
        mPollListAdapter = new PollFragment.PollListAdapter(this.getActivity(), polls);
        // Attach the adapter to the recyclerview to populate items
        mPollListRecycleView.setAdapter(mPollListAdapter);
        // Set layout manager to position the items
        mLinearLayoutManager = new LinearLayoutManager(this.getActivity());
        mPollListRecycleView.setLayoutManager(mLinearLayoutManager);
    }

    // Create the basic adapter extending from RecyclerView.Adapter
    // Note that we specify the custom ViewHolder which gives us access to our views
    public class PollListAdapter extends
            RecyclerView.Adapter<PollFragment.PollListAdapter.ViewHolder> {

        private ArrayList<Poll> mPolls;
        private Context mContext;
        private int lastPosition = -1;

        // Pass in the contact array into the constructor
        public PollListAdapter(Context context, ArrayList<Poll> polls) {
            mPolls = polls;
            mContext = context;
        }

        // Easy access to the context object in the recyclerview
        private Context getContext() {
            return mContext;
        }

        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        public class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public TextView mPollTitleTextView;
            public TextView mPollQuestionTextView;
            public RecyclerView mPollAnswerRecyclerView;
            public Button mVoteButton;
            public ImageButton menuButton;


            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                mPollTitleTextView = (TextView) itemView.findViewById(R.id.tv_poll_title);
                mPollQuestionTextView = (TextView) itemView.findViewById(R.id.tv_poll_question);
                mPollAnswerRecyclerView = (RecyclerView) itemView.findViewById(R.id.rv_poll_answer_list);
                mVoteButton = (Button) itemView.findViewById(R.id.button_vote);
                menuButton = itemView.findViewById(R.id.menu_button);
            }

        }

        @Override
        public PollFragment.PollListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View view = inflater.inflate(R.layout.poll_list_item, parent, false);

            // Return a new holder instance
            PollFragment.PollListAdapter.ViewHolder viewHolder = new PollFragment.PollListAdapter.ViewHolder(view);
            return viewHolder;
        }

        // Involves populating data into the item through holder
        @Override
        public void onBindViewHolder(PollFragment.PollListAdapter.ViewHolder viewHolder, final int position) {
            // Get the data model based on position
            final Poll poll = mPolls.get(position);

            final PollAnswerListAdapter mPollAnswerListAdapter = new PollAnswerListAdapter(this.mContext, poll.getPollAnswers(), viewHolder, poll);
            viewHolder.mPollAnswerRecyclerView.setAdapter(mPollAnswerListAdapter);
            viewHolder.mPollAnswerRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.mContext, LinearLayoutManager.VERTICAL, false);
            viewHolder.mPollAnswerRecyclerView.setLayoutManager(layoutManager);

            viewHolder.mPollTitleTextView.setText(poll.getTitle());
            viewHolder.mPollQuestionTextView.setText(poll.getQuestion());

            // bind button events
            viewHolder.mVoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPollAnswerListAdapter.selectedPosition == -1){
                        return;
                    }
                    Database.getInstance().answerPoll(currentRoomId, poll.getKey(), String.valueOf(mPollAnswerListAdapter.selectedPosition), AppUtils.getDeviceId(getContext()));
                }
            });

            setAnimation(viewHolder.itemView, position);

            // hide menu button
            if (isCreator) {
                viewHolder.menuButton.setVisibility(View.VISIBLE);
            } else {
                viewHolder.menuButton.setVisibility(View.GONE);
            }

            // bind menu button
            viewHolder.menuButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    showPopup(view);
                }

                public void showPopup(View v) {
                    PopupMenu popup = new PopupMenu(getContext(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_poll_card, popup.getMenu());

                    // add listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId()) {
                                case R.id.menu_item_poll_edit:
                                    showDialog(poll.getKey());
                                    break;
                                case R.id.menu_item_poll_delete:
                                    deletePoll(poll.getKey());
                            }
                            return false;
                        }
                    });

                    popup.show();
                }
            });

        }

        private void deletePoll(String pollId){
            Database.getInstance().deletePoll(currentRoomId, pollId);
        }

        @Override
        public int getItemCount() {
            return mPolls.size();
        }

        private void setAnimation(View view, int position){
            if (position > lastPosition) {
                AnimationSet anims = new AnimationSet(false);

                AlphaAnimation fade = new AlphaAnimation(0.0f, 1.0f);
                fade.setDuration(200);
                anims.addAnimation(fade);

                view.startAnimation(anims);
                lastPosition = position;
            }
        }

    }

    public class PollAnswerListAdapter extends
            RecyclerView.Adapter<PollFragment.PollAnswerListAdapter.ViewHolder> {

        private ArrayList<PollAnswer> mPollAnswers;
        private Context mContext;
        private PollFragment.PollListAdapter.ViewHolder mParentViewHolder;
        private Poll parentPoll;

        public int selectedPosition = -1;
        public int unselectedPosition = -1;

        // Pass in the contact array into the constructor
        public PollAnswerListAdapter(Context context, ArrayList<PollAnswer> pollAnswers, PollFragment.PollListAdapter.ViewHolder parentViewHolder, Poll poll) {
            mPollAnswers = pollAnswers;
            mContext = context;
            mParentViewHolder = parentViewHolder;
            parentPoll = poll;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public TextView mAnswerTextView;
            public TextView mVoterCount;
            public View mBarCount;
            public View mBarFrame;
            public LinearLayout mLayoutAnswerList;
            public ImageView mIconCheckVote;

            public int measuredWidth;
            public int measuredHeight;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                mAnswerTextView = (TextView) itemView.findViewById(R.id.tv_answer);
                mVoterCount = (TextView) itemView.findViewById(R.id.tv_voter_count);
                mBarCount = (View) itemView.findViewById(R.id.bar_count);
                mBarFrame = (View) itemView.findViewById(R.id.fl_bar);
                mLayoutAnswerList = itemView.findViewById(R.id.layout_answer_list);
                mIconCheckVote = itemView.findViewById(R.id.icon_check_vote);
            }
        }

        @Override
        public PollAnswerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View view = inflater.inflate(R.layout.poll_answer_list_item, parent, false);

            // Return a new holder instance
            final PollFragment.PollAnswerListAdapter.ViewHolder viewHolder = new PollFragment.PollAnswerListAdapter.ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final PollAnswerListAdapter.ViewHolder holder, final int position) {
            final PollAnswer answer = mPollAnswers.get(position);
            if (answer == null){
                return;
            }

            int voterCount = 0;
            if (answer.getVoters() != null){
                voterCount = answer.countVoters();
            }

            holder.mAnswerTextView.setText(answer.getAnswer());
            holder.mVoterCount.setText(voterCount + " votes");

            // TODO: Make it dynamic width ..
            int maxWidth = 870;
            int currentWidth = voterCount * maxWidth / 10;
            holder.mBarCount.getLayoutParams().width = currentWidth;


            // style changing
            if (answer.getVoters() != null
                    && answer.getVoters().get(AppUtils.getDeviceId(getContext())) != null
                    && answer.getVoters().get(AppUtils.getDeviceId(getContext()))){
                holder.mAnswerTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                holder.mIconCheckVote.setImageResource(R.drawable.check_active);
            }

            else {
                holder.mAnswerTextView.setTextColor(getResources().getColor(R.color.textGray));
                holder.mIconCheckVote.setImageResource(R.drawable.check_inactive);
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = position;


                    if (answer.getVoters() != null
                            && answer.getVoters().get(AppUtils.getDeviceId(getContext())) != null
                            && answer.getVoters().get(AppUtils.getDeviceId(getContext()))) {
                        Database.getInstance().unanswerPoll(currentRoomId, parentPoll.getKey(), String.valueOf(selectedPosition), AppUtils.getDeviceId(getContext()));
                    } else {
                        Database.getInstance().answerPoll(currentRoomId, parentPoll.getKey(), String.valueOf(selectedPosition), AppUtils.getDeviceId(getContext()));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPollAnswers.size();
        }
    }
}