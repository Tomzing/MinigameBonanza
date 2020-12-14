package hiof.prosjekt.minigamebonanza.ui.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import hiof.prosjekt.minigamebonanza.R;
import hiof.prosjekt.minigamebonanza.data.model.Minigame;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.MinigameStatusNotificationFragment;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.MinigameStarFragment;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.MinigameStatusbarFragment;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.PreMinigameFragment;
import hiof.prosjekt.minigamebonanza.ui.main.utility.MinigameUtility;
import hiof.prosjekt.minigamebonanza.ui.main.utility.NotificationBuilder;
import hiof.prosjekt.minigamebonanza.ui.main.utility.NotificationChannelCreator;
import hiof.prosjekt.minigamebonanza.ui.main.viewmodel.StatusbarViewModel;


public class MinigameStarActivity extends AppCompatActivity {

    Minigame minigame = new Minigame(2, "Test Minigame", "Placeholder", 10);
    public final static ArrayList<Integer> COMPLETED_MINIGAMES = new ArrayList<>();
    int runOnce = 0;
    boolean isRunning = false;
    int goldStarsPressed = 0;
    boolean showNotification;


    TextView timeRemainingText, timeRemainingNmbr, pointsText, attemptsRemainingText, minigameDescText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_background);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        int currentOrientation = this.getResources().getConfiguration().orientation;
        // Handles the orientation based on the orientation when the minigame is started
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        showNotification = true;

        minigame = new Minigame(2, "Star Minigame", getResources().getString(R.string.minigame_star_description), 10);
        startMinigame();
    }

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

    Runnable minigameRunnable = new Runnable() {
        public void run() {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MinigameStarFragment.newInstance())
                    .commitNow();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MinigameStatusbarFragment.newInstance())
                    .commitNow();

            initMinigameView();
            startMinigameTimer();
            isRunning = true;
            Log.i("tag", "This'll run 3000 milliseconds later");
        }
    };

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

        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
        anim.setDuration(1000); //Put desired duration per anim cycle here, in milliseconds

        ImageView goldStar1 = findViewById(R.id.goldStarImageView1);
        goldStar1.setSoundEffectsEnabled(false);
        goldStar1.startAnimation(anim);

        goldStar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView goldStar1 = findViewById(R.id.goldStarImageView1);
                goldStar1.clearAnimation();
                goldStar1.setVisibility(View.GONE);
                goldStarsPressed++;
                hasPlayerPressedFiveStars(goldStarsPressed, false);
                dingSoundEffectPlayer();
            }
        });

        ImageView goldStar2 = findViewById(R.id.goldStarImageView2);
        goldStar2.setSoundEffectsEnabled(false);
        goldStar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView goldStar2 = findViewById(R.id.goldStarImageView2);
                goldStar2.setVisibility(View.GONE);
                goldStarsPressed++;
                hasPlayerPressedFiveStars(goldStarsPressed, false);
                dingSoundEffectPlayer();

            }
        });

        ImageView goldStar3 = findViewById(R.id.goldStarImageView3);
        goldStar3.setSoundEffectsEnabled(false);
        goldStar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView goldStar1 = findViewById(R.id.goldStarImageView3);
                goldStar1.setVisibility(View.GONE);
                goldStarsPressed++;
                hasPlayerPressedFiveStars(goldStarsPressed, false);
                dingSoundEffectPlayer();

            }
        });

        ImageView goldStar4 = findViewById(R.id.goldStarImageView4);
        goldStar4.setSoundEffectsEnabled(false);
        goldStar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView goldStar4 = findViewById(R.id.goldStarImageView4);
                goldStar4.setVisibility(View.GONE);
                goldStarsPressed++;
                hasPlayerPressedFiveStars(goldStarsPressed, false);
                dingSoundEffectPlayer();

            }
        });

        ImageView goldStar5 = findViewById(R.id.goldStarImageView5);
        goldStar5.setSoundEffectsEnabled(false);
        goldStar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView goldStar5 = findViewById(R.id.goldStarImageView5);
                goldStar5.setVisibility(View.GONE);
                goldStarsPressed++;
                hasPlayerPressedFiveStars(goldStarsPressed, false);
                dingSoundEffectPlayer();

            }
        });
    }

    public boolean hasPlayerPressedFiveStars(int goldStarsPressed, boolean isTest) {
        if(goldStarsPressed == 5 ) {
            if(!isTest) {
                succeedMinigame();
            }
            return true;
        }
        else {
            return false;
        }
    }

    public void dingSoundEffectPlayer() {
        SharedPreferences sharedPreference = getSharedPreferences("hiof.prosjekt.minigamebonanza_preferences", MODE_PRIVATE);
        if(!sharedPreference.getBoolean("muteSoundFx", false)) {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.dingsound);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            mediaPlayer.setAudioAttributes(audioAttributes);
            mediaPlayer.start();
            //mediaPlayer.release();
        }

    }

    public void succeedMinigame() {

        StatusbarViewModel mViewModel = new ViewModelProvider(this).get(StatusbarViewModel.class);
        showNotification = false;
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

            Intent intent = new Intent(getApplicationContext(), MinigameShakeActivity.class);
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

            goldStarsPressed = 0;

        }
        else {
            Log.i("tag","No attempts remaining, go to results screen");

            showNotification = false;
            cancelMinigame();

            Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
            Bundle extras = new Bundle();

            extras.putInt("ATTEMPTS_REMAINING", Integer.parseInt(mViewModel.getAttemptsRemaining()));
            extras.putInt("SCORE", Integer.parseInt(mViewModel.getScore()));
            extras.putIntegerArrayList("COMPLETED_MINIGAMES", COMPLETED_MINIGAMES);
            intent.putExtras(extras);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    // Remove the callbacks from the threads in order to stop potential exceptions
    public void cancelMinigame() {
        minigameHandler.removeCallbacks(minigameRunnable);
        failureNotificationHandler.removeCallbacks(failureNotificationRunnable);
        successNotificationHandler.removeCallbacks(successNotificationRunnable);
        isRunning = false;
        cdt.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        showNotification = true;
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences sharedPreference = getSharedPreferences("hiof.prosjekt.minigamebonanza_preferences", MODE_PRIVATE);

        NotificationChannelCreator.createNotificationChannel(this);

        // Check if the notification is supposed to be showing and wether user has set notifications
        // on or not. This is true by default. I'm sorry.
        if(showNotification && sharedPreference.getBoolean("notification", true)) {
            NotificationBuilder.createNotificationBuilder(this);
        }
    }

    // When user presses back, ensures the back button goes to main menu instead of previous minigame, if applicable
    @Override
    public void onBackPressed() {
        cancelMinigame();
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        showNotification = false;
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    // When activity is destroyed, remove callbacks and cancel the timer in order to prevent crashes
    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelMinigame();
        Log.i("tag","ONDESTROY INITIATED");
    }
}