package group_8.project_evnt;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class PollFragment extends Fragment {

    private FragmentActivity myContext;

    public PollFragment () {
    }

    public static PollFragment newInstance() {
        PollFragment fragment = new PollFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_poll, container, false);

        Button newPollButton = (Button) rootView.findViewById(R.id.new_poll_button);

        final LinearLayout l1 = (LinearLayout) rootView.findViewById(R.id.linear_layout_container);

        newPollButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Button button = new Button(getActivity());
//                button.setText("POLL");
//
//                LinearLayout.LayoutParams params =
//                        new LinearLayout.LayoutParams(
//                                ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.WRAP_CONTENT);
//                button.setLayoutParams(params);
//                button.setGravity(Gravity.CENTER_HORIZONTAL);
//                button.setTextSize(32);

                // l1.addView(button);

                // Begin the transaction
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                // Replace the contents of the container with the new fragment
                ft.add(R.id.linear_layout_container, new AddPollFragment());
                // Complete the changes added above
                ft.commit();
            }
        });

        return rootView;
    }
}