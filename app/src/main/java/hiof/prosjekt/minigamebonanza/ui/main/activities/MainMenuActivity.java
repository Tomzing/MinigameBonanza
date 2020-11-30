package hiof.prosjekt.minigamebonanza.ui.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import hiof.prosjekt.minigamebonanza.R;
import hiof.prosjekt.minigamebonanza.data.model.Minigame;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.MainFragment;
import hiof.prosjekt.minigamebonanza.ui.main.utility.NotificationBuilder;
import hiof.prosjekt.minigamebonanza.ui.main.utility.NotificationChannelCreator;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
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
        requestPermissions();
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
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //Toast.makeText(this,"The app has access to your coarse location.",Toast.LENGTH_LONG).show();

        }
        else  {

            Toast.makeText(this,"This app requires to know your last known location to make one of the minigames to work properly.",Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this,
                    new String[] { ACCESS_FINE_LOCATION },
                    1);
        }
    }

    public void startGameOnclick(View v) {
        Log.i(TAG, "Trykket på startgame");
        showNotification = false;
        Intent intent = new Intent(getApplicationContext(), MinigameStarActivity.class);
        //Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
        //Intent intent = new Intent(getApplicationContext(), MinigameShakeActivity.class);
        //Intent intent = new Intent(getApplicationContext(), MinigameLocationActivity.class);

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
                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                startActivity(intent);
        }
    }

    public void quitBtnOnClick(View v) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        //createNotificationChannel();
    }

}