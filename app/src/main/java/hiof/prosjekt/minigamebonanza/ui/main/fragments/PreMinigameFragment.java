package hiof.prosjekt.minigamebonanza.ui.main.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hiof.prosjekt.minigamebonanza.R;

public class PreMinigameFragment extends Fragment {

    private static final String ARG_GAMEDESC = "param1";


    private String minigameDesc;


    public PreMinigameFragment() {
        // Required empty public constructor
    }

    public static PreMinigameFragment newInstance(String param1) {
        PreMinigameFragment fragment = new PreMinigameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GAMEDESC, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            minigameDesc = getArguments().getString(ARG_GAMEDESC);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_pre_minigame, container, false);
        TextView gameDesc = (TextView) returnView.findViewById(R.id.minigameDescText);
        gameDesc.setText(getArguments().getString(ARG_GAMEDESC));
        // Inflate the layout for this fragment
        return returnView;
    }
}