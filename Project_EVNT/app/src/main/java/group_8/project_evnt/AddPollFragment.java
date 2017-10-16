package group_8.project_evnt;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import group_8.project_evnt.core.Database;
import group_8.project_evnt.models.ChatMessage;
import group_8.project_evnt.models.PollAlternative;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPollFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPollFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_POLL_ID = "pollid";
    private String currentPollId;
    private ArrayList<PollAlternative> pollAlternatives = new ArrayList<>();

    private RecyclerView mPollAlternativeRecycleView;
    private Button mAddAltButton;

    private LinearLayoutManager mLinearLayoutManager;
    private AddAlternativeAdapter mAddAlternativeAdapter;

    public AddPollFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddPollFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPollFragment newInstance(String pollId) {
        AddPollFragment fragment = new AddPollFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POLL_ID, pollId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            currentPollId = getArguments().getString(ARG_POLL_ID);
        } else {
            return;
        }

        pollAlternatives.add(new PollAlternative());
        mAddAlternativeAdapter.notifyDataSetChanged();

//        DatabaseReference poll = Database.getInstance().poll(currentPollId);
//        poll.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                pollAlternatives.clear();
//                for (DataSnapshot alternative : dataSnapshot.getChildren()){
//                    Log.d("Data", alternative.toString());
//                    pollAlternatives.add(alternative.getValue(PollAlternative.class));
//                }
//                if(mAddAlternativeAdapter != null) {
//                    mAddAlternativeAdapter.notifyDataSetChanged();
//                }
//                if(mLinearLayoutManager != null) {
//                    mLinearLayoutManager.scrollToPosition(pollAlternatives.size() - 1);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_poll, container, false);

        mPollAlternativeRecycleView = view.findViewById(R.id.rv_alternatives);
        mAddAltButton = view.findViewById(R.id.add_alt_button);
        mAddAltButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup adapter
        // Create adapter passing in the sample user data
        mAddAlternativeAdapter = new AddAlternativeAdapter(this.getActivity(), pollAlternatives);
        // Attach the adapter to the recyclerview to populate items
        mPollAlternativeRecycleView.setAdapter(mAddAlternativeAdapter);
        // Set layout manager to position the items
        mLinearLayoutManager = new LinearLayoutManager(this.getActivity());
        mPollAlternativeRecycleView.setLayoutManager(mLinearLayoutManager);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            // when sending new message
            case R.id.add_alt_button:
                pollAlternatives.add(new PollAlternative());
                mAddAlternativeAdapter.notifyDataSetChanged();
                break;
        }
    }

    // Create the basic adapter extending from RecyclerView.Adapter
    // Note that we specify the custom ViewHolder which gives us access to our views
    public class AddAlternativeAdapter extends
            RecyclerView.Adapter<AddAlternativeAdapter.ViewHolder> {

        private ArrayList<PollAlternative> mPollAlternatives;
        private Context mContext;

        // Pass in the contact array into the constructor
        public AddAlternativeAdapter(Context context, ArrayList<PollAlternative> pollAlternatives) {
            mPollAlternatives = pollAlternatives;
            mContext = context;
        }

        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        public class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public TextView text_plus_sign;
            public EditText et_alternative;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                text_plus_sign = (TextView) itemView.findViewById(R.id.plus_sign);
                et_alternative = (EditText) itemView.findViewById(R.id.add_alternative);
            }
        }

        @Override
        public AddPollFragment.AddAlternativeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View yourMessageView = inflater.inflate(R.layout.fragment_add_alt, parent, false);

            // Return a new holder instance
            AddPollFragment.AddAlternativeAdapter.ViewHolder viewHolder = new AddPollFragment.AddAlternativeAdapter.ViewHolder(yourMessageView);
            return viewHolder;
        }

        // Involves populating data into the item through holder
        @Override
        public void onBindViewHolder(AddPollFragment.AddAlternativeAdapter.ViewHolder viewHolder, int position) {
            // Get the data model based on position
            PollAlternative alt = mPollAlternatives.get(position);

            // Set item views based on your views and data model
            TextView alternative = viewHolder.et_alternative;
            alternative.setText(alt.getAlternative());
        }

        @Override
        public int getItemCount() {
            return mPollAlternatives.size();
        }
    }
}
