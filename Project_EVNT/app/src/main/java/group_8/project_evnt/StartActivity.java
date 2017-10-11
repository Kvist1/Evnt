package group_8.project_evnt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

import group_8.project_evnt.core.Database;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void onCreateButtonClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.key_room_id), Database.getInstance().createRoom());
        startActivity(intent);
    }

}
