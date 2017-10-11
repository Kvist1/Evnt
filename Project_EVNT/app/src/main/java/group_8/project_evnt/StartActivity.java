package group_8.project_evnt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import group_8.project_evnt.core.Database;

import android.view.View;

import java.util.Random;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        DatabaseReference chat = Database.getInstance().chat("MY_NEW_ROOM_ID");
        chat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.toString();

                if (value != null){
                    Log.i("LENGTH: ", String.valueOf(dataSnapshot.getChildrenCount()));
                    Log.i("VALUES: ", value);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Database.getInstance().writeChatMessage("MY_NEW_ROOM_ID", "Felix Franzén", "This is my new message");
        String newRoomId = Database.getInstance().createRoom();
        Log.i("THE NEW ROOM ID: ", newRoomId);
        Database.getInstance().writeChatMessage(newRoomId, "Felix Franzén", "This is my new message");

    }

    public void onCreateButtonClick(View view) {
        // TODO: Call to firebae to create a room and get the room id
        Random random = new Random();
        // generate a random integer from 0 to 999
        Integer x = random.nextInt(999);
        String roomId = x.toString();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.key_room_id), roomId);
        startActivity(intent);
    }

}
