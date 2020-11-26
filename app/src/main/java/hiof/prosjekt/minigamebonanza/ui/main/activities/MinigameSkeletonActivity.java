package hiof.prosjekt.minigamebonanza.ui.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import hiof.prosjekt.minigamebonanza.R;
import hiof.prosjekt.minigamebonanza.data.model.Minigame;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.MinigameStatusNotificationFragment;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.MinigameStarFragment;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.PreMinigameFragment;
import hiof.prosjekt.minigamebonanza.ui.main.utility.MinigameUtility;
import hiof.prosjekt.minigamebonanza.ui.main.viewmodel.StatusbarViewModel;


public class MinigameSkeletonActivity extends AppCompatActivity {

    Minigame minigame1 = new Minigame(3,"Skeleton minigame","Press all the golden stars in order to succeed",40);
    public final static ArrayList<Integer> COMPLETED_MINIGAMES = new ArrayList<>();
    int runOnce = 0;
    boolean isRunning = false;

    TextView timeRemainingText, timeRemainingNmbr, pointsText, attemptsRemainingText, minigameDescText;
    Runnable minigameRunnable = new Runnable() {
        public void run() {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MinigameStarFragment.newInstance())
                    .commitNow();

            initMinigameView();
            startMinigameTimer();
            isRunning = true;
            Log.i("tag", "This'll run 3000 milliseconds later");
        }
    };
    Handler minigameHandler = new Handler();
    CountDownTimer cdt = new CountDownTimer(minigame1.getTime()*1000, 500) {
        public void onTick(long millisUntilFinished) {
            // Used for formatting digit to be in 2 digits only
            timeRemainingText = findViewById(R.id.timeRemainingText);
            timeRemainingNmbr = findViewById(R.id.timeRemainingNmbr);
            NumberFormat f = new DecimalFormat("0");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_background);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        startMinigame();
    }

    public void startMinigame() {
        //Before the minigame, a black box with white text explaining the minigame
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PreMinigameFragment.newInstance(minigame1.getDescription()))
                .commitNow();

        // Opens the actual minigame on a timer. After 3 seconds it'll start the timer
        if(!isRunning) {
            minigameHandler.postDelayed(minigameRunnable, 3000);
        }
        else {
            minigameHandler.removeCallbacks(minigameRunnable);
            isRunning = false;
        }
    }

    // Initializes the minigame into the view
    public void initMinigameView() {

        StatusbarViewModel mViewModel = new ViewModelProvider(this).get(StatusbarViewModel.class);

        Intent prevIntent = getIntent();
        Bundle extras = prevIntent.getExtras();
        int attemptsRemaining = extras.getInt("ATTEMPTS_REMAINING");
        int score = extras.getInt("SCORE");

        // Points and attempts remaining gets information from the extras put into the intent
        if(runOnce == 0) {
            runOnce = 1;
            mViewModel.setScore(score);
            mViewModel.setAttemptsReamining(attemptsRemaining);

            attemptsRemainingText = findViewById(R.id.attemptsRemainingText);
            attemptsRemainingText.append(" " + mViewModel.getAttemptsRemaining());

            pointsText = findViewById((R.id.pointsText));
            pointsText.setText(mViewModel.getScore());
        }
        // Already extracted information from extras put into intent, use viewmodel instead
        else {
            attemptsRemainingText = findViewById(R.id.attemptsRemainingText);
            attemptsRemainingText.append(" " + mViewModel.getAttemptsRemaining());

            pointsText = findViewById((R.id.pointsText));
            pointsText.setText(mViewModel.getScore());
        }
    }

    public void dingSoundEffectPlayer() {

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.dingsound);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        mediaPlayer.setAudioAttributes(audioAttributes);
        mediaPlayer.start();
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

        cancelMinigame();

        int score = MinigameUtility.calculatePoints(minigame1.getTime(), Integer.parseInt((String) timeRemainingNmbr.getText()));
        mViewModel.setScore(score);

        // Edit the path of the intent to go to the next minigame
        Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
        Bundle extras = new Bundle();

        extras.putInt("ATTEMPTS_REMAINING", Integer.parseInt(mViewModel.getAttemptsRemaining()));
        extras.putInt("SCORE", Integer.parseInt(mViewModel.getScore()));
        extras.putIntegerArrayList("COMPLETED_MINIGAMES", COMPLETED_MINIGAMES);
        intent.putExtras(extras);
        startActivity(intent);

        //TODO implement method for going to next minigame, if no minigames left go to results screen
    }

    // Method for failing a minigame
    public void failMinigame() {
        StatusbarViewModel mViewModel = new ViewModelProvider(this).get(StatusbarViewModel.class);

        //Check for remaining attempts
        if(Integer.parseInt(mViewModel.getAttemptsRemaining()) != 0) {
            Log.i("tag","Fail minigame triggered with more than 0 attempts");

            cancelMinigame();

            mViewModel.setAttemptsReamining(Integer.parseInt(mViewModel.getAttemptsRemaining())-1);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MinigameStatusNotificationFragment.newInstance(false))
                    .commitNow();

            //Handler to restart the minigame, after having shown the failure notification on-screen
            Handler failureNotificationHandler = new Handler();
            Runnable failureNotificationRunnable = new Runnable() {
                public void run() {
                    startMinigame();
                }
            };

            failureNotificationHandler.postDelayed(failureNotificationRunnable, 3000);

        }
        else {
            Log.i("tag","No attempts remaining, go to results screen");

            cancelMinigame();

            Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
            Bundle extras = new Bundle();

            extras.putInt("ATTEMPTS_REMAINING", Integer.parseInt(mViewModel.getAttemptsRemaining()));
            extras.putInt("SCORE", Integer.parseInt(mViewModel.getScore()));
            extras.putIntegerArrayList("COMPLETED_MINIGAMES", COMPLETED_MINIGAMES);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }

    public void cancelMinigame() {
        minigameHandler.removeCallbacks(minigameRunnable);
        isRunning = false;
        cdt.cancel();
    }

    // When user presses back, ensures the back button goes to main menu instead of previous minigame, if applicable
    @Override
    public void onBackPressed() {
        cancelMinigame();
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
    }

    // When activity is destroyed, remove callbacks and cancel the timer in order to prevent crashes
    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelMinigame();
        Log.i("tag","ONDESTROY INITIATED");
    }
}