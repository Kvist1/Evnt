package group_8.project_evnt;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import group_8.project_evnt.core.Database;
import group_8.project_evnt.models.ChatMessage;
import group_8.project_evnt.models.Poll;

public class PollFragment extends Fragment {
    private String currentRoomId;
    private ArrayList<Poll> polls;

    private static final String ARG_ROOM_ID = "roomid";

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

        DatabaseReference poll = Database.getInstance().poll(currentRoomId);
        poll.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                polls.clear();
                for (DataSnapshot message : dataSnapshot.getChildren()){
                    Log.d("Data", message.toString());
//                    polls.add(message.getValue(ChatMessage.class));
                }
//                if(mChatMessageAdapter != null) {
//                    mChatMessageAdapter.notifyDataSetChanged();
//                }
//                if(mLinearLayoutManager != null) {
//                    mLinearLayoutManager.scrollToPosition(chatMessages.size() - 1);
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_poll, container, false);


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
}