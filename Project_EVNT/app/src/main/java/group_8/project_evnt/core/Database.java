package group_8.project_evnt.core;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    public DatabaseReference chat(){
        return database.getReference("chat");
    }


}
