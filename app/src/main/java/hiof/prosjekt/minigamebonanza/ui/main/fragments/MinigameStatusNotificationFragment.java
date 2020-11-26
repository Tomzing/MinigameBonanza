package hiof.prosjekt.minigamebonanza.ui.main.fragments;

import androidx.annotation.ColorRes;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import hiof.prosjekt.minigamebonanza.R;


public class MinigameStatusNotificationFragment extends Fragment {

    public static MinigameStatusNotificationFragment newInstance(boolean isFailure) {

        MinigameStatusNotificationFragment fragment = new MinigameStatusNotificationFragment();
        Bundle args = new Bundle();
        args.putBoolean("ARG_FAILURE", isFailure);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View returnView = inflater.inflate(R.layout.fragment_status_notification, container, false);
        TextView messageTextView = (TextView) returnView.findViewById(R.id.messageTextView);
        if(getArguments().getBoolean("ARG_FAILURE")) {
            messageTextView.setText(R.string.minigame_status_failure_message);
        }
        else {
            messageTextView.setText(R.string.minigame_status_success_message);

            messageTextView.getBackground().setTint(Color.rgb(0,100,0));
            //messageTextView.setTextColor(Color.rgb(213,209,200));
            messageTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        return returnView;
    }

}
