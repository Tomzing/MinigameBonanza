package hiof.prosjekt.minigamebonanza.ui.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.squareup.seismic.ShakeDetector;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import hiof.prosjekt.minigamebonanza.R;
import hiof.prosjekt.minigamebonanza.data.model.Minigame;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.MinigameStatusNotificationFragment;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.MinigameBlankFragment;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.PreMinigameFragment;
import hiof.prosjekt.minigamebonanza.ui.main.utility.MinigameUtility;
import hiof.prosjekt.minigamebonanza.ui.main.viewmodel.StatusbarViewModel;


public class MinigameShakeActivity extends AppCompatActivity implements ShakeDetector.Listener{

    Minigame minigame = new Minigame(3,"Ball minigame","Shake your phone 10 times",20);
    public final static ArrayList<Integer> COMPLETED_MINIGAMES = new ArrayList<>();
    int runOnce = 0;
    boolean isRunning = false;
    int timesShaken = 0;

    SensorManager sensorManager;
    ShakeDetector sd;

    TextView timeRemainingText, timeRemainingNmbr, pointsText, attemptsRemainingText, minigameDescText, timesShakenTextView;

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

        minigame = new Minigame(3,"Shake minigame",getResources().getString(R.string.minigame_shake_description),20);
        startMinigame();

    }

    Runnable minigameRunnable = new Runnable() {
        public void run() {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MinigameBlankFragment.newInstance())
                    .commitNow();

            initMinigameView();
            startMinigameTimer();
            isRunning = true;

            Log.i("tag", "This'll run 3000 milliseconds later");
        }
    };
    Handler minigameHandler = new Handler();
    CountDownTimer cdt = new CountDownTimer(minigame.getTime()*1000, 500) {
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

    public void startMinigame() {
        //Before the minigame, a black box with white text explaining the minigame
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PreMinigameFragment.newInstance(minigame.getDescription()))
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
        timesShaken = 0;
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
            enableShakeDetector();
        }
        // Already extracted information from extras put into intent, use viewmodel instead
        else {
            attemptsRemainingText = findViewById(R.id.attemptsRemainingText);
            attemptsRemainingText.append(" " + mViewModel.getAttemptsRemaining());

            pointsText = findViewById((R.id.pointsText));
            pointsText.setText(mViewModel.getScore());
            enableShakeDetector();
        }
    }

    // Enables the shake detector library to listen to shakes
    public void enableShakeDetector() {
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Checks if the phone running the app has a gyroscope
        if(accelerometerSensor != null) {
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            sd = new ShakeDetector(this);
            sd.setSensitivity(ShakeDetector.SENSITIVITY_LIGHT);
            sd.start(sensorManager);
        }
        else {
            succeedMinigame();
        }
    }

    public void dingSoundEffectPlayer() {

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.dingsound2);

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

    public void succeedMinigame() {

        StatusbarViewModel mViewModel = new ViewModelProvider(this).get(StatusbarViewModel.class);

        cancelMinigame();

        int score = MinigameUtility.calculatePoints(minigame.getTime(), Integer.parseInt((String) timeRemainingNmbr.getText()));
        mViewModel.setScore(score);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, MinigameStatusNotificationFragment.newInstance(false))
                .commitNow();

        successNotificationHandler.postDelayed(successNotificationRunnable, 3000);

        //TODO implement method for going to next minigame, if no minigames left go to results screen
    }

    Handler successNotificationHandler = new Handler();
    Runnable successNotificationRunnable = new Runnable() {
        public void run() {
            StatusbarViewModel mViewModel = getStatusBarViewmodel();

            Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
            Bundle extras = new Bundle();

            extras.putInt("ATTEMPTS_REMAINING", Integer.parseInt(mViewModel.getAttemptsRemaining()));
            extras.putInt("SCORE", Integer.parseInt(mViewModel.getScore()));
            extras.putIntegerArrayList("COMPLETED_MINIGAMES", COMPLETED_MINIGAMES);
            intent.putExtras(extras);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            cancelMinigame();
        }
    };

    // A quick and dirty workaround to get the viewmodel within a runnable
    public StatusbarViewModel getStatusBarViewmodel() {
        return new ViewModelProvider(this).get(StatusbarViewModel.class);
    }

    Handler failureNotificationHandler = new Handler();
    Runnable failureNotificationRunnable = new Runnable() {
        public void run() {
            startMinigame();
        }
    };

    // Method for failing a minigame
    public void failMinigame() {
        StatusbarViewModel mViewModel = new ViewModelProvider(this).get(StatusbarViewModel.class);
        timesShaken = 0;

        //Check for remaining attempts
        if(Integer.parseInt(mViewModel.getAttemptsRemaining()) != 0) {
            Log.i("tag","Fail minigame triggered with more than 0 attempts");

            cancelMinigame();

            mViewModel.setAttemptsReamining(Integer.parseInt(mViewModel.getAttemptsRemaining())-1);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MinigameStatusNotificationFragment.newInstance(true))
                    .commitNow();

            //Handler to restart the minigame, after having shown the failure notification on-screen
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
        failureNotificationHandler.removeCallbacks(failureNotificationRunnable);
        successNotificationHandler.removeCallbacks(successNotificationRunnable);
        isRunning = false;
        sensorManager.unregisterListener(sd);
        cdt.cancel();
    }

    @Override
    protected void onPause() {

        super.onPause();
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

    // When a shake is detected, hearShake runs
    @Override
    public void hearShake() {

        // Seperated the validation to a seperate method in order to tie a test to it
        validateShakes(timesShaken, false);

    }

    public boolean validateShakes(int timesShakenCheck, boolean isTest) {
        if(timesShakenCheck < 10) {
            if(!isTest) {
                timesShaken++;
                dingSoundEffectPlayer();
                timesShakenTextView = findViewById(R.id.timesShakenTextView);
                timesShakenTextView.setText(Integer.toString(timesShaken));
            }

            return false;
        }
        else if(timesShakenCheck <= 10) {
            if(!isTest) {
                succeedMinigame();
            }
        }
        return true;
    }
}