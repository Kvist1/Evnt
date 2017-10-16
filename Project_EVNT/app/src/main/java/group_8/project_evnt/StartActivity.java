package group_8.project_evnt;

import android.content.Intent;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import group_8.project_evnt.core.Database;
import group_8.project_evnt.models.Room;
import group_8.project_evnt.utils.AppUtils;

public class StartActivity extends AppCompatActivity {

    FrameLayout progressBarHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        EditText editText = (EditText)findViewById(R.id.room_id);
        editText.setTransformationMethod(null);
        editText.setImeActionLabel("GO", KeyEvent.KEYCODE_ENTER);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    progressBarHolder.setVisibility(View.VISIBLE);
                    Database.getInstance().findRoom(textView.getText().toString(), new Database.CreateRoomCallbackInterface() {
                        @Override
                        public void onRoomRetrieved(Room room) {
                            progressBarHolder.setVisibility(View.GONE);
                            if(room != null) {
                                Log.d("ROOM", room.getRoomCode());
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra(getString(R.string.key_room_id), room.getRoomId());
                                intent.putExtra(getString(R.string.key_room_code), room.getRoomCode());
                                startActivity(intent);
                            } else {
                                // toast the not found message
                                Toast.makeText(getApplicationContext(), "Room not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    return false;
                }
                return false;
            }
        });

        Log.d("Device Id", AppUtils.getDeviceId(this));
    }

    public void onCreateButtonClick(View view) {
        progressBarHolder.setVisibility(View.VISIBLE);
        Database.getInstance().createRoom(this, new Database.CreateRoomCallbackInterface() {
            public void onRoomRetrieved(Room room) {
                progressBarHolder.setVisibility(View.GONE);
                goToRoom(room);
            }
        });
    }

    private void goToRoom(Room room) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(getString(R.string.key_room_id), room.getRoomId());
        intent.putExtra(getString(R.string.key_room_code), room.getRoomCode());
        startActivity(intent);
    }



}
