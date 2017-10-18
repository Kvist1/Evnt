package group_8.project_evnt;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class PollFragment extends Fragment {
    private String currentRoomId;
    private ArrayList<Poll> polls = new ArrayList<Poll>();

    private static final String ARG_ROOM_ID = "roomid";

    private RecyclerView mPollListRecycleView;

    private LinearLayoutManager mLinearLayoutManager;
    private PollFragment.PollListAdapter mPollListAdapter;

    private FragmentActivity myContext;

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

        // mockup data for poll
        PollAnswer a1 = new PollAnswer("Alternative 1");
        PollAnswer a2 = new PollAnswer("Alternative 2");
        ArrayList<PollAnswer> answers = new ArrayList<PollAnswer>();
//        ArrayList<String> voters = new ArrayList<String>();
        a1.addVoter("testid1");
        a1.addVoter("testid2");
        answers.add(a1);
        answers.add(a2);
        Poll p1 = new Poll("Title-1", "Question-1", answers, true);
        Poll p2 = new Poll("Title-2", "Question-2", answers, false);
        polls.add(p1);
//        polls.add(p2);
        //

        DatabaseReference poll = Database.getInstance().poll(currentRoomId);
        poll.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                polls.clear();
                for (DataSnapshot p : dataSnapshot.getChildren()){
                    Log.d("Data", p.toString());
                    polls.add(p.getValue(Poll.class));
                }
                if(mPollListAdapter != null) {
                    mPollListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_poll, container, false);

        // findViewById stuff
        // Inflate the layout for this fragment
        mPollListRecycleView = rootView.findViewById(R.id.rv_poll_list);

        FloatingActionButton newPollFab = (FloatingActionButton) rootView.findViewById(R.id.new_poll_fab);
        final LinearLayout l1 = (LinearLayout) rootView.findViewById(R.id.linear_layout_container);

        newPollFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Begin the transaction
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                // Replace the contents of the container with the new fragment
                ft.add(R.id.linear_layout_container, new AddPollFragment());
                // Complete the changes added above
                ft.commit();
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

        private static final int ITEM_TYPE_NORMAL = 0;
        private static final int ITEM_TYPE_CREATOR = 1;

        private ArrayList<Poll> mPolls;
        private Context mContext;
//        private PollAnswerListAdapter mPollAnswerListAdapter;

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

            }
        }

//        public int getItemViewType(int position) {
//            if (mChatMessages.get(position).isCreator()) {
//                return ITEM_TYPE_CREATOR;
//            } else {
//                return ITEM_TYPE_NORMAL;
//            }
//        }

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
        public void onBindViewHolder(PollFragment.PollListAdapter.ViewHolder viewHolder, int position) {
            // Get the data model based on position
            Poll poll = mPolls.get(position);

            final PollAnswerListAdapter mPollAnswerListAdapter = new PollAnswerListAdapter(this.mContext, poll.getPollAnwsers());
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
                    Log.d("SELECTED ANSWER", mPollAnswerListAdapter.selectedPosition + " ");
                }
            });

        }

        @Override
        public int getItemCount() {
            return mPolls.size();
        }

    }

    public class PollAnswerListAdapter extends
            RecyclerView.Adapter<PollFragment.PollAnswerListAdapter.ViewHolder> {

        private ArrayList<PollAnswer> mPollAnswers;
        private Context mContext;

        public int selectedPosition = -1;

        // Pass in the contact array into the constructor
        public PollAnswerListAdapter(Context context, ArrayList<PollAnswer> pollAnswers) {
            mPollAnswers = pollAnswers;
            mContext = context;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public TextView mAnswerTextView;
            public TextView mVoterCount;
            public View mBarCount;
            public View mBarFrame;

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
            }
        }

        @Override
        public PollAnswerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View view = inflater.inflate(R.layout.poll_answer_list_item, parent, false);

            // Return a new holder instance
            PollFragment.PollAnswerListAdapter.ViewHolder viewHolder = new PollFragment.PollAnswerListAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(PollAnswerListAdapter.ViewHolder holder, final int position) {
            PollAnswer answer = mPollAnswers.get(position);
            int voterCount = answer.getVoters().size();
            holder.mAnswerTextView.setText(answer.getAnswer());
            holder.mVoterCount.setText(voterCount + " votes");

            // get the width ref
            int maxWidth = holder.mBarFrame.getLayoutParams().width;
            if(maxWidth > 0) {
                int currentWidth = voterCount / maxWidth;
                holder.mBarCount.getLayoutParams().width = currentWidth;
            }

            if(selectedPosition == position)
                holder.mAnswerTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            else
                holder.mAnswerTextView.setTextColor(getResources().getColor(R.color.textGray));

            holder.mAnswerTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = position;
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPollAnswers.size();
        }
    }
}