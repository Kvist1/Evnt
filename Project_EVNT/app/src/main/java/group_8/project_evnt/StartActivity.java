package group_8.project_evnt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
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
