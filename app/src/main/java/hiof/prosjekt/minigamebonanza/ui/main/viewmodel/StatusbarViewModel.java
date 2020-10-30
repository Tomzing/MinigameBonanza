package hiof.prosjekt.minigamebonanza.ui.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import hiof.prosjekt.minigamebonanza.data.model.Statusbar;

public class StatusbarViewModel extends ViewModel {
    private final SavedStateHandle mState;
    private int attemptsRemaining, score;

    public StatusbarViewModel(SavedStateHandle savedStateHandle) {
        mState = savedStateHandle;
        if(mState.get("AttemptsRemaining") == null) {
            activeStatusbar = new Statusbar(3,0);
            mState.set("AttemptsRemaining","3");
            mState.set("Score","0");
        }
    }

    private Statusbar activeStatusbar;

    public Statusbar getStatusbar() {
        if(mState.get("AttemptsRemaining") == null) {
            activeStatusbar = new Statusbar(3,0);
            mState.set("AttemptsRemaining","3");
            mState.set("Score","0");
        }
        return activeStatusbar;
    }

    public void decreaseAttemptsRemaining() {
        String attemptsRemaining = Integer.toString(activeStatusbar.getAttemptsRemaining()-1);
        activeStatusbar.setAttemptsRemaining(activeStatusbar.getAttemptsRemaining()-1);
        mState.set("AttemptsRemaining",attemptsRemaining);
    }

    public void setScore(int score) {
        int currentScore = Integer.parseInt(getScore()) + score;
        mState.set("Score",currentScore);
    }

    /*public int getAttemptsRemaining() {
        return Integer.parseInt((String) mState.get("AttemptsRemaining"));
    }*/

    public String getAttemptsRemaining() {
        System.out.println("Attempts remaining: " + mState.get("AttemptsRemaining"));
        return mState.get("AttemptsRemaining");
    }

    public String getScore() {
        //System.out.println(mState.get("Score"));
        //return Integer.parseInt((String) mState.get("Score"));
        return String.valueOf(mState.get("Score"));
    }
}
