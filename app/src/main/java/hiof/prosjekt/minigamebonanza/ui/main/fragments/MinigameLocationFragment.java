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
import hiof.prosjekt.minigamebonanza.ui.main.Minigame1ViewModel;

public class MinigameLocationFragment extends Fragment {
    private Minigame1ViewModel mViewModel;

    public static MinigameLocationFragment newInstance() {
        return new MinigameLocationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_minigame_country, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Minigame1ViewModel.class);
    }
}
