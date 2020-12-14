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

public class ChooseGamemodeFragment extends Fragment {

    public static ChooseGamemodeFragment newInstance() {
        return new ChooseGamemodeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_gamemode_fragment, container, false);
    }
}