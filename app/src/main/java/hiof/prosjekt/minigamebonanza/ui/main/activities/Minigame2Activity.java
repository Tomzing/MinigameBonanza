package hiof.prosjekt.minigamebonanza.ui.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import hiof.prosjekt.minigamebonanza.R;
import hiof.prosjekt.minigamebonanza.data.model.Minigame;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.FailureNotificationFragment;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.MinigameStarFragment;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.PreMinigameFragment;
import hiof.prosjekt.minigamebonanza.ui.main.utility.MinigameUtility;
import hiof.prosjekt.minigamebonanza.ui.main.viewmodel.StatusbarViewModel;


public class Minigame2Activity extends AppCompatActivity {

    Minigame minigame1 = new Minigame(2,"Test Minigame","Press all the golden stars in order to succeed",2);
    public final static ArrayList<Integer> COMPLETED_MINIGAMES = new ArrayList<>();
    int runOnce = 0;
    boolean isRunning = false;
    int goldStarsPressed = 0;

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

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.dingsound);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        mediaPlayer.setAudioAttributes(audioAttributes);
        mediaPlayer.start();

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

        //mViewModel.setScore(10);
        int score = MinigameUtility.calculatePoints(minigame1.getTime(), Integer.parseInt((String) timeRemainingNmbr.getText()));
        mViewModel.setScore(score);

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

            goldStarsPressed = 0;

            cancelMinigame();

            mViewModel.setAttemptsReamining(Integer.parseInt(mViewModel.getAttemptsRemaining())-1);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, FailureNotificationFragment.newInstance())
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