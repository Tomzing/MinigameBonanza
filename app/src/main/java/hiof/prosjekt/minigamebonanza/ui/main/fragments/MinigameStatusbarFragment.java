package hiof.prosjekt.minigamebonanza.ui.main.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hiof.prosjekt.minigamebonanza.R;
import hiof.prosjekt.minigamebonanza.ui.main.viewmodel.StatusbarViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MinigameStatusbarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MinigameStatusbarFragment extends Fragment {

    public MinigameStatusbarFragment() {
        // Required empty public constructor
    }

    public static MinigameStatusbarFragment newInstance() {
        MinigameStatusbarFragment fragment = new MinigameStatusbarFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_minigame_status, container, false);
    }
}