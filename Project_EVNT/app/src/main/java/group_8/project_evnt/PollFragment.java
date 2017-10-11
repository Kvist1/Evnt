package group_8.project_evnt;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class PollFragment extends Fragment {

    public PollFragment () {
    }

    public static PollFragment newInstance() {
        PollFragment fragment = new PollFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_poll, container, false);

        Button newPollButton = (Button) rootView.findViewById(R.id.new_poll_button);

        return rootView;
    }
}