package hiof.prosjekt.minigamebonanza.ui.main.fragments;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import hiof.prosjekt.minigamebonanza.R;
import hiof.prosjekt.minigamebonanza.ui.main.Minigame1ViewModel;


public class Minigame1Fragment extends Fragment {
    private Minigame1ViewModel mViewModel;

    public static Minigame1Fragment newInstance() {
        return new Minigame1Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_minigame1, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Minigame1ViewModel.class);
        // TODO: Use the ViewModel
    }
}
