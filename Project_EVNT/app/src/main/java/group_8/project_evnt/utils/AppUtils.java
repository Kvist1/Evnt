package group_8.project_evnt.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import java.util.UUID;

/**
 * Created by kittipon on 10/16/17.
 */

public class AppUtils {
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public synchronized static String getDeviceId(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
//        String androidId = Settings.Secure.getString(context.getContentResolver(),
//                Settings.Secure.ANDROID_ID);
//
//        return androidId;
    }
}
