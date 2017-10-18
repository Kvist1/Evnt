package group_8.project_evnt;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import group_8.project_evnt.core.Database;
import group_8.project_evnt.models.ChatMessage;
import group_8.project_evnt.models.Room;
import group_8.project_evnt.utils.AppUtils;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment implements View.OnClickListener  {
    private static final String ARG_ROOM_ID = "roomid";
    private String currentRoomId;
    private ArrayList<ChatMessage> chatMessages = new ArrayList<>();


    private RecyclerView mChatMessageRecycleView;
    private ImageButton mSendMessageButton;
    private EditText mMessageInputEditText;

    private LinearLayoutManager mLinearLayoutManager;
    private ChatMessageAdapter mChatMessageAdapter;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String roomId) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROOM_ID, roomId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            currentRoomId = getArguments().getString(ARG_ROOM_ID);
        } else {
            return;
        }

        DatabaseReference chat = Database.getInstance().chat(currentRoomId);
        chat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatMessages.clear();
                for (DataSnapshot message : dataSnapshot.getChildren()){
                    Log.d("Data", message.toString());
                    chatMessages.add(message.getValue(ChatMessage.class));
                }
                if(mChatMessageAdapter != null) {
                    mChatMessageAdapter.notifyDataSetChanged();
                }
                if(mLinearLayoutManager != null) {
                    mLinearLayoutManager.scrollToPosition(chatMessages.size() - 1);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mChatMessageRecycleView = view.findViewById(R.id.rv_chat_message);
        mSendMessageButton = view.findViewById(R.id.button_send_message);
        //set onClick listener
        mSendMessageButton.setOnClickListener(this);
        mMessageInputEditText = view.findViewById(R.id.et_message_input);

        mMessageInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                if (charSequence.length() != 0){
                    mSendMessageButton.setAlpha(new Float(1));
                } else {
                    mSendMessageButton.setAlpha(new Float(0.25));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup adapter
        // Create adapter passing in the sample user data
        mChatMessageAdapter = new ChatMessageAdapter(this.getActivity(), chatMessages);
        // Attach the adapter to the recyclerview to populate items
        mChatMessageRecycleView.setAdapter(mChatMessageAdapter);
        // Set layout manager to position the items
        mLinearLayoutManager = new LinearLayoutManager(this.getActivity());
        mChatMessageRecycleView.setLayoutManager(mLinearLayoutManager);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // when sending new message
            case R.id.button_send_message:
                final String msg = mMessageInputEditText.getText().toString();
                if(msg.isEmpty()) {
                    return;
                }

                final Database db = Database.getInstance();
                final String userId = AppUtils.getDeviceId(getContext());
                Log.i("--------currentRoomId: ", currentRoomId);
                db.findRoomById(currentRoomId, new Database.CreateRoomCallbackInterface() {
                    @Override
                    public void onRoomRetrieved(Room room) {
                        Log.i("--------USERID: ", userId);
                        Log.i("--------USERID; ", room.getCreator());
                        if (room != null) {
                            boolean isCreator = room.getCreator().equals(userId);
                            db.writeChatMessage(currentRoomId, userId, msg, isCreator);
                        }
                    }
                });

                mMessageInputEditText.setText("");
                break;
        }
    }

    // Create the basic adapter extending from RecyclerView.Adapter
    // Note that we specify the custom ViewHolder which gives us access to our views
    public class ChatMessageAdapter extends
            RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

        private static final int ITEM_TYPE_YOUR_MESSAGE = 0;
        private static final int ITEM_TYPE_OTHER_MESSAGE = 1;
        private static final int ITEM_TYPE_CREATOR_MESSAGE = 2;
        private int lastPosition = -1;

        private ArrayList<ChatMessage> mChatMessages;
        private Context mContext;

        // Pass in the contact array into the constructor
        public ChatMessageAdapter(Context context, ArrayList<ChatMessage> chatMessages) {
            mChatMessages = chatMessages;
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
            public TextView mMessageTextView;
            public TextView mTimeStampTextView;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                mMessageTextView = (TextView) itemView.findViewById(R.id.tv_message);
                mTimeStampTextView = (TextView) itemView.findViewById(R.id.tv_timestamp);
            }
        }

        public int getItemViewType(int position) {
            ChatMessage message = mChatMessages.get(position);
            if (message.getUserId().equals(AppUtils.getDeviceId(getContext()))) {
                return ITEM_TYPE_YOUR_MESSAGE;
            } else if (message.isCreator()) {
                return ITEM_TYPE_CREATOR_MESSAGE;
            } else {
                return ITEM_TYPE_OTHER_MESSAGE;
            }
        }

        @Override
        public ChatMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.i("VIEW TYPE: ", String.valueOf(viewType));
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View messageView;

            switch (viewType){
                case 0: messageView = inflater.inflate(R.layout.chat_your_message_item, parent, false); break;
                case 1: messageView = inflater.inflate(R.layout.chat_other_message_item, parent, false); break;
                case 2: messageView = inflater.inflate(R.layout.chat_creator_message, parent, false); break;
                default: messageView = inflater.inflate(R.layout.chat_other_message_item, parent, false); break;

            }

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(messageView);
            return viewHolder;
        }

        // Involves populating data into the item through holder
        @Override
        public void onBindViewHolder(ChatMessageAdapter.ViewHolder viewHolder, int position) {
            // Get the data model based on position
            ChatMessage chatMessage = mChatMessages.get(position);


            // Set item views based on your views and data model
            TextView message = viewHolder.mMessageTextView;
            message.setText(chatMessage.getMessage());

            // Format and set the chat message timestamp
            TextView time = viewHolder.mTimeStampTextView;
            long ms = chatMessage.getTime();
            Date date = new Date(ms);
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            time.setText(dateFormat.format(date));

            setAnimation(viewHolder.itemView, position);

        }

        @Override
        public int getItemCount() {
            return mChatMessages.size();
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

}
