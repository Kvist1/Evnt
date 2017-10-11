package group_8.project_evnt.core;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import group_8.project_evnt.models.ChatMessage;

/**
 * Created by Felix on 2017-10-09.
 */

public class Database {
    private static final Database ourInstance = new Database();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
    }

    public DatabaseReference chat(String roomId){
        return database.getReference().child("chats").child(roomId);
    }

    public DatabaseReference writeChatMessage(String roomId, String userId, String message){
        ChatMessage newMessage = new ChatMessage(userId, message);

        DatabaseReference chatReference = database.getReference().child("chats").child(roomId);
        String msgKey = chatReference.push().getKey();
        chatReference.child(msgKey).setValue(newMessage);

        return chatReference.child(msgKey);

    }

}
