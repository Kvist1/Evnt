package group_8.project_evnt;

import android.content.Intent;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

public class StartActivity extends AppCompatActivity implements TextWatcher{
    private EditText editText;
    FrameLayout progressBarHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        editText = (EditText)findViewById(R.id.room_id);
        editText.setTransformationMethod(null);
        editText.setImeActionLabel("GO", KeyEvent.KEYCODE_ENTER);
        editText.addTextChangedListener(this);
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
        Database.getInstance().createRoom(AppUtils.getDeviceId(this), new Database.CreateRoomCallbackInterface() {
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


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        View line1 = findViewById(R.id.hint_line1);
        View line2 = findViewById(R.id.hint_line2);
        View line3 = findViewById(R.id.hint_line3);

        if (editText.getText().length() == 0){
            Log.d("Textlängden är ", editText.getText().length() + "");
            line1.setBackgroundColor(getColor(R.color.colorAccent));
            line2.setBackgroundColor(getColor(R.color.mainCardColor));
            line3.setBackgroundColor(getColor(R.color.mainCardColor));
        }

        if (editText.getText().length() == 1){
            line1.setBackgroundColor(getColor(R.color.mainCardColor));
            line2.setBackgroundColor(getColor(R.color.colorAccent));
            line3.setBackgroundColor(getColor(R.color.mainCardColor));
        }
        else if (editText.getText().length() == 2){
            line1.setBackgroundColor(getColor(R.color.mainCardColor));
            line2.setBackgroundColor(getColor(R.color.mainCardColor));
            line3.setBackgroundColor(getColor(R.color.colorAccent));
        }
        else if (editText.getText().length() == 3){
            line1.setBackgroundColor(getColor(R.color.colorAccent));
            line2.setBackgroundColor(getColor(R.color.colorAccent));
            line3.setBackgroundColor(getColor(R.color.colorAccent));
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
