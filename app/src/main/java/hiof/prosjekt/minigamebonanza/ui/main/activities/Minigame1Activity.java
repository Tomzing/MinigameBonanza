package hiof.prosjekt.minigamebonanza.ui.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import hiof.prosjekt.minigamebonanza.R;
import hiof.prosjekt.minigamebonanza.data.model.Minigame;
import hiof.prosjekt.minigamebonanza.ui.main.Minigame1ViewModel;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.Minigame1Fragment;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.PreMinigameFragment;
import hiof.prosjekt.minigamebonanza.ui.main.utility.MinigameUtility;
import hiof.prosjekt.minigamebonanza.ui.main.viewmodel.StatusbarViewModel;

import static android.content.ContentValues.TAG;
import static hiof.prosjekt.minigamebonanza.R.id.completeBtn;
import static hiof.prosjekt.minigamebonanza.R.id.start;
import static hiof.prosjekt.minigamebonanza.R.id.stop;
import static hiof.prosjekt.minigamebonanza.R.id.timeRemainingNmbr;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Minigame1Activity extends AppCompatActivity {

    Minigame minigame1 = new Minigame(1,"Test Minigame","Quickly press the button to cheat your way to victory",10);

    TextView timeRemainingText, timeRemainingNmbr, pointsText, attemptsRemainingText, minigameDescText;
    Runnable minigameRunnable = new Runnable() {
        public void run() {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Minigame1Fragment.newInstance())
                    .commitNow();

            initMinigameView();
            startMinigameTimer();
            isRunning = true;
            Log.i("tag", "This'll run 300 milliseconds later");
        }
    };
    Handler minigameHandler = new Handler();
    CountDownTimer cdt = new CountDownTimer(minigame1.getTime()*1000, 500) {
        public void onTick(long millisUntilFinished) {
            // Used for formatting digit to be in 2 digits only
            timeRemainingText = findViewById(R.id.timeRemainingText);
            timeRemainingNmbr = findViewById(R.id.timeRemainingNmbr);
            NumberFormat f = new DecimalFormat("0");
            long hour = (millisUntilFinished / 3600000) % 24;
            long min = (millisUntilFinished / 60000) % 60;
            long sec = (millisUntilFinished / 1000) % 60;
            timeRemainingNmbr.setText(f.format(sec));
            if(Integer.parseInt(f.format(sec)) <  4) {
                timeRemainingText.setTextColor(Color.RED);
                timeRemainingNmbr.setTextColor(Color.RED);
            }
            else if(Integer.parseInt(f.format(sec)) == 0) {
                timeRemainingText.setText("Failure");
            }
        }
        // When timer is done, run this. This'll be a failure condition
        public void onFinish() {
            //timeRemainingNmbr.setText("Dang");
            failMinigame();
        }
    };
    boolean isRunning = false;

    public void initMinigameView() {

        StatusbarViewModel mViewModel = new ViewModelProvider(this).get(StatusbarViewModel.class);

        //Points and attempts remaining gets infromation from the viewmodel
        pointsText = findViewById((R.id.pointsText));
        //pointsText.append(Integer.toString(mViewModel.getScore()));
        pointsText.setText(mViewModel.getScore());
        //pointsText.append(" 10");

        attemptsRemainingText = findViewById(R.id.attemptsRemainingText);
        attemptsRemainingText.append(" " + mViewModel.getAttemptsRemaining());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_background);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        //model = new ViewModelProvider(this).get(StatusbarViewModel.class);



        startMinigame();
    }

    public void startMinigame() {
        //Before the minigame, a black box with white text explaining the minigame
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PreMinigameFragment.newInstance(minigame1.getDescription()))
                .commitNow();

        // Opens the actual minigame on a timer
        if(!isRunning) {
            minigameHandler.postDelayed(minigameRunnable, 3000);
        }
        else {
            minigameHandler.removeCallbacks(minigameRunnable);
            isRunning = false;
        }

    }

    //Starts the minigame timer
    public void startMinigameTimer() {

        if(!isRunning) {
            Log.i("tag","Minigame timer triggered!");
            cdt.start();
        }
        else {
            cdt.cancel();
        }
    }

    public void btnSuccess(View v) {
        Log.i("tag", "Pressed complete minigame");
        succeedMinigame();
    }

    public void btnFail(View v) {
        Log.i("tag","Fail button pressed");
        failMinigame();
    }

    public void succeedMinigame() {

        StatusbarViewModel mViewModel = new ViewModelProvider(this).get(StatusbarViewModel.class);

        //mViewModel.setScore(10);
        int score = MinigameUtility.calculatePoints(minigame1.getTime(), Integer.parseInt((String) timeRemainingNmbr.getText()));
        mViewModel.setScore(score);

        //TODO implement method for going to next minigame, if no minigames left go to results screen
    }

    // Method for failing a minigame
    public void failMinigame() {
        StatusbarViewModel mViewModel = new ViewModelProvider(this).get(StatusbarViewModel.class);
        if(Integer.parseInt(mViewModel.getAttemptsRemaining()) != 0) {
            Log.i("tag","Fail minigame triggered with more than 0 attempts");
            minigameHandler.removeCallbacks(minigameRunnable);
            mViewModel.decreaseAttemptsRemaining();
            isRunning = false;
            cdt.cancel();

            //Intent intent = new Intent(this, Minigame1Activity.class);
            //startActivity(intent);
            startMinigame();
        }
        else {
            Log.i("tag","No attempts remaining, go to results screen");
            //TODO implement results screen
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        minigameHandler.removeCallbacks(minigameRunnable);
        cdt.cancel();
        Log.i("tag","ONDESTROY INITIATED");
    }
}