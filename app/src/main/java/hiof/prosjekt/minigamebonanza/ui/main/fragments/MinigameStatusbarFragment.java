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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MinigameStatusbarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_minigame_status.
     */
    // TODO: Rename and change types and number of parameters
    public static MinigameStatusbarFragment newInstance(String param1, String param2) {
        MinigameStatusbarFragment fragment = new MinigameStatusbarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        /*StatusbarViewModel mViewModel = ViewModelProviders.of(this).get(StatusbarViewModel.class);
        TextView pointsText = getView().findViewById(R.id.pointsText);
        pointsText.append(Integer.toString(mViewModel.getAttemptsRemaining()));
         */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_minigame_status, container, false);
    }
}