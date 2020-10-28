package hiof.prosjekt.minigamebonanza.ui.main.viewmodel;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import hiof.prosjekt.minigamebonanza.data.model.Statusbar;

public class StatusbarViewModel extends ViewModel {
    private SavedStateHandle mState;

    public StatusbarViewModel(SavedStateHandle savedStateHandle) {
        mState = savedStateHandle;
    }

    private Statusbar activeStatusbar;

    public Statusbar getStatusbar() {
        if(activeStatusbar == null) {
            activeStatusbar = new Statusbar(3,0);
        }
        return activeStatusbar;
    }

    public void decreaseAttemptsRemaining() {
        activeStatusbar.setAttemptsRemaining(activeStatusbar.getAttemptsRemaining()-1);
    }

    public void setScore(int score) {
        activeStatusbar.setScore(score);
    }

    public int getAttemptsRemaining() {
        return activeStatusbar.getAttemptsRemaining();
    }

    public int getScore() {
        return activeStatusbar.getScore();
    }
}
