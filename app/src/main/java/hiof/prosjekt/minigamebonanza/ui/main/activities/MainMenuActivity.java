package hiof.prosjekt.minigamebonanza.ui.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import hiof.prosjekt.minigamebonanza.R;
import hiof.prosjekt.minigamebonanza.data.model.Minigame;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.MainFragment;

import static android.content.ContentValues.TAG;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_background);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
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
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void startGameOnclick(View v) {
        Log.i(TAG, "Trykket på startgame");
        Intent intent = new Intent(getApplicationContext(), Minigame1Activity.class);
        Minigame minigame1 = new Minigame(1,"Test Minigame","Quickly press the button to cheat your way to victory",10);

        //MotionLayout motionLayout = v.findViewById(R.id.startGamePressed);
        //((MotionLayout)v.findViewById(R.id.startGamePressed)).transitionToEnd();
        //((MotionLayout)findViewById(R.id.startGamePressedEnd)).transitionToEnd();

        startActivity(intent);
    }

    public void startSettingsOnClick(View v) {
        switch (v.getId()) {
            case R.id.startSettingsBtn:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);

                startActivity(intent);
                Log.i(TAG, "Trykket på settings");
        }
    }

    public void startLeaderboardOnClick(View v) {
        switch (v.getId()) {
            case R.id.startLeaderboardBtn:
                Intent intent = new Intent(getApplicationContext(), LeaderboardActivity.class);

                startActivity(intent);
        }
    }

}