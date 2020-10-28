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

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Minigame1Activity extends AppCompatActivity {

    TextView timeRemainingText, pointsText, attemptsRemainingText, minigameDescText;

    private Minigame1ViewModel model;

    Minigame minigame1 = new Minigame(1,"Test Minigame","Quickly press the button to cheat your way to victory",10);
    //Minigame1ViewModel viewModel = ViewModelProviders.of(this).get(Minigame1ViewModel.class);


    public void initMinigameView() {

        //StatusbarViewModel mViewModel = ViewModelProviders.of(this).get(StatusbarViewModel.class);
        StatusbarViewModel mViewModel = new ViewModelProvider(this).get(StatusbarViewModel.class);
        //timeRemainingText = findViewById(R.id.timeRemainingText);
        //timeRemainingText.append(" " + minigame1.getTime() + "s");

        //Points and attempts remaining gets infromation from the viewmodel
        pointsText = findViewById((R.id.pointsText));
        pointsText.append(Integer.toString(mViewModel.getStatusbar().getScore()));
        //pointsText.append(" 10");

        attemptsRemainingText = findViewById(R.id.attemptsRemainingText);
        attemptsRemainingText.append(Integer.toString(mViewModel.getStatusbar().getAttemptsRemaining()));
    }

    public void initPreView() {
        minigameDescText = findViewById(R.id.minigameDescText);
        //minigameDescText.setText("minigame1.getDescription()");
        //minigameDescText.append("text");
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

        //Before the minigame, a black box with white text explaining the minigame
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PreMinigameFragment.newInstance("hei","nei"))
                .commitNow();
        initPreView();

        // Opens the actual minigame on a timer
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, Minigame1Fragment.newInstance())
                                    .commitNow();

                        initMinigameView();

                        //Starts the minigame timer
                        timeRemainingText = findViewById(R.id.timeRemainingText);
                        new CountDownTimer(minigame1.getTime()*1000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                // Used for formatting digit to be in 2 digits only
                                NumberFormat f = new DecimalFormat("00");
                                long hour = (millisUntilFinished / 3600000) % 24;
                                long min = (millisUntilFinished / 60000) % 60;
                                long sec = (millisUntilFinished / 1000) % 60;
                                timeRemainingText.setText("Time remaining: " +  f.format(sec));
                                if(Integer.parseInt(f.format(sec)) <  4) {
                                    timeRemainingText.setTextColor(Color.RED);
                                }
                            }
                            // When timer is done, run this. This'll be a failure condition
                            public void onFinish() {
                                timeRemainingText.setText("Dang");
                                failMinigame();
                            }
                        }.start();
                        Log.i("tag", "This'll run 300 milliseconds later");
                    }
                },
                1000);

    }

    public void btnHandler(View v) {
        switch (v.getId()) {
            case R.id.completeBtn:
                Log.i("tag", "Pressed success");
            case R.id.failBtn:
                Log.i("tag","Fail button pressed");
        }
    }

    public void succeedMinigame() {

        StatusbarViewModel mViewModel = new ViewModelProvider(this).get(StatusbarViewModel.class);


        //mViewModel.setScore(MinigameUtility.calculatePoints());
    }

    // Method for failing a minigame
    public void failMinigame() {
        //StatusbarViewModel mViewModel = ViewModelProviders.of(this).get(StatusbarViewModel.class);
        StatusbarViewModel mViewModel = new ViewModelProvider(this).get(StatusbarViewModel.class);
        if(mViewModel.getAttemptsRemaining() != 0) {
            mViewModel.decreaseAttemptsRemaining();

            Intent intent = new Intent(getApplicationContext(), Minigame1Activity.class);
            startActivity(intent);
        }
        else {
            //TODO implement results screen
        }

    }
}