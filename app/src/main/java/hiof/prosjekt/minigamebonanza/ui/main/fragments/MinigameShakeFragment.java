package hiof.prosjekt.minigamebonanza.ui.main.fragments;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import hiof.prosjekt.minigamebonanza.R;

public class MinigameShakeFragment extends Fragment {

    public static MinigameShakeFragment newInstance() {
        return new MinigameShakeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_minigame_shake, container, false);
    }
}
