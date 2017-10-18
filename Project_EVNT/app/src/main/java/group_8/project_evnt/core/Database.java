package group_8.project_evnt.core;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Random;

import group_8.project_evnt.models.ChatMessage;
import group_8.project_evnt.models.Room;
import group_8.project_evnt.utils.AppUtils;

/**
 * Created by Felix on 2017-10-09.
 */

public class Database {
    private static final Database ourInstance = new Database();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static Database getInstance() {
        return ourInstance;
    }

    public interface CreateRoomCallbackInterface {
        void onRoomRetrieved(Room room);
    }

    private Database() {
    }

    public DatabaseReference rooms(){
        return database.getReference().child("rooms");
    }

    public void findRoom(String roomCode, final CreateRoomCallbackInterface callback) {
        DatabaseReference roomRef = rooms();

        roomRef.orderByChild("roomCode").equalTo(roomCode).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("ROOM_", "Should be call only once");
                if(dataSnapshot.getValue() != null) {
                    Room firstRoom = null;
                    for(DataSnapshot room : dataSnapshot.getChildren() ){
                        firstRoom = room.getValue(Room.class);
                        if (firstRoom.getRoomId()!= null) {
                            break;
                        }
                    }
                    callback.onRoomRetrieved(firstRoom);
                } else {
                    callback.onRoomRetrieved(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void createRoom(final Context context, final CreateRoomCallbackInterface callback ){
        DatabaseReference roomRef = rooms();

        // Attach a listener to read the data at our posts reference
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long code = dataSnapshot.getChildrenCount() + 1;
                String roomCode = String.format("%03d", code);
                String roomId = rooms().push().getKey();
                Room room = new Room(roomId, roomCode, AppUtils.getDeviceId(context));
                rooms().child(roomId).setValue(room);

                //
                callback.onRoomRetrieved(room);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public DatabaseReference chat(String roomId){
        return database.getReference().child("chats").child(roomId);
    }

    public DatabaseReference writeChatMessage(String roomId, String userId, String message, boolean isCreator){
        ChatMessage newMessage = new ChatMessage(userId, message, isCreator);

        DatabaseReference chatReference = database.getReference().child("chats").child(roomId);
        String msgKey = chatReference.push().getKey();
        chatReference.child(msgKey).setValue(newMessage);

        return chatReference.child(msgKey);

    }

}
