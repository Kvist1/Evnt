package group_8.project_evnt.utils;

import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Felix on 2017-10-18.
 */

public abstract class DebouncedOnEditorAction implements TextView.OnEditorActionListener{

    private final long minimumInterval;
    private Map<View, Long> lastClickMap;

    public abstract void OnDebouncedEditorAction(TextView textView, int i, KeyEvent keyEvent);


    public DebouncedOnEditorAction(long minimumIntervalMsec) {
            this.minimumInterval = minimumIntervalMsec;
            this.lastClickMap = new WeakHashMap<View, Long>();
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        Log.i("ONEDITORACTION:", "ONEDTORACTION IN DEBOUNCE!!!!!!!");
        Long previousEditTimestamp = lastClickMap.get(textView);
        long currentTimestamp = SystemClock.uptimeMillis();

        lastClickMap.put(textView, currentTimestamp);
        if(previousEditTimestamp == null || (currentTimestamp - previousEditTimestamp.longValue() > minimumInterval)) {
            OnDebouncedEditorAction(textView, i, keyEvent);
        }

        return true;
    }
}
