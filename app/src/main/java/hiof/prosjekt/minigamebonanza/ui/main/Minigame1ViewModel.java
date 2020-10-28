package hiof.prosjekt.minigamebonanza.ui.main;

import androidx.lifecycle.ViewModel;

import hiof.prosjekt.minigamebonanza.data.model.Statusbar;

public class Minigame1ViewModel extends ViewModel {

    private Statusbar activeStatusbar;

    public Statusbar getStatusbar() {
        if(activeStatusbar == null) {
            activeStatusbar = new Statusbar(3,0);
        }
        return activeStatusbar;
    }
}