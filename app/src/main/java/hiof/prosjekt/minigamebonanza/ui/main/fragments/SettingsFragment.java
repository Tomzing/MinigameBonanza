package hiof.prosjekt.minigamebonanza.ui.main.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import hiof.prosjekt.minigamebonanza.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}