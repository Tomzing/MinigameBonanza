package hiof.prosjekt.minigamebonanza.ui.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;

import hiof.prosjekt.minigamebonanza.R;
import hiof.prosjekt.minigamebonanza.data.model.Minigame;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.MainFragment;
import hiof.prosjekt.minigamebonanza.ui.main.utility.NotificationBuilder;
import hiof.prosjekt.minigamebonanza.ui.main.utility.NotificationChannelCreator;

import static android.content.ContentValues.TAG;

public class MainMenuActivity extends AppCompatActivity {

    public final static ArrayList<Integer> COMPLETED_MINIGAMES = new ArrayList<>();
    boolean showNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_background);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }

        showNotification = false;
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

    public void startGameOnclick(View v) {
        Log.i(TAG, "Trykket på startgame");
        showNotification = false;
        //Intent intent = new Intent(getApplicationContext(), MinigameStarActivity.class);
        Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
        //Intent intent = new Intent(getApplicationContext(), MinigameShakeActivity.class);
        Minigame minigame1 = new Minigame(1,"Test Minigame","Quickly press the button to cheat your way to victory",10);

        //MotionLayout motionLayout = v.findViewById(R.id.startGamePressed);
        //((MotionLayout)v.findViewById(R.id.startGamePressed)).transitionToEnd();
        //((MotionLayout)findViewById(R.id.startGamePressedEnd)).transitionToEnd();

        Bundle extras = new Bundle();

        extras.putInt("ATTEMPTS_REMAINING", 3);
        extras.putInt("SCORE", 0);
        extras.putIntegerArrayList("COMPLETED_MINIGAMES", COMPLETED_MINIGAMES);
        intent.putExtras(extras);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void startSettingsOnClick(View v) {
        showNotification = false;
        switch (v.getId()) {
            case R.id.startSettingsBtn:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);

                startActivity(intent);
                Log.i(TAG, "Trykket på settings");
        }
    }

    public void startLeaderboardOnClick(View v) {
        showNotification = false;
        switch (v.getId()) {
            case R.id.startLeaderboardBtn:
                Intent intent = new Intent(getApplicationContext(), LeaderboardActivity.class);

                startActivity(intent);
        }
    }

    public void quitBtnOnClick(View v) {
        finish();
    }

    /*private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel name";
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("minigameBonanza", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }*/

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

    @Override
    public void onDestroy() {
        super.onDestroy();


        //createNotificationChannel();


    }

}