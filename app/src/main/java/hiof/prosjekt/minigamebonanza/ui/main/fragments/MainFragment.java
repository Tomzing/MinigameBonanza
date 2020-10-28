package hiof.prosjekt.minigamebonanza.ui.main.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hiof.prosjekt.minigamebonanza.R;
import hiof.prosjekt.minigamebonanza.ui.main.Minigame1ViewModel;

public class MainFragment extends Fragment  {

    private Minigame1ViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mainmenu, container, false);

        return inflater.inflate(R.layout.fragment_mainmenu, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Minigame1ViewModel.class);
        // TODO: Use the ViewModel
    }

}