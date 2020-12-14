package hiof.prosjekt.minigamebonanza.ui.main.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import hiof.prosjekt.minigamebonanza.R;
import hiof.prosjekt.minigamebonanza.ui.main.fragments.ResultsFragment;
import hiof.prosjekt.minigamebonanza.ui.main.utility.NotificationBuilder;
import hiof.prosjekt.minigamebonanza.ui.main.utility.NotificationChannelCreator;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static java.util.List.of;

public class ResultsActivity extends AppCompatActivity {

    TextView scoreResultsText, attemptsResultsText;
    int REQUESTED_PERMISSION = 1;
    EditText inputName;
    String inputNameString = "";
    boolean showNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);


        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        showNotification = true;
        initResultsView();
    }

    public void initResultsView() {
        Intent prevIntent = getIntent();
        Bundle extras = prevIntent.getExtras();
        int attemptsRemaining = extras.getInt("ATTEMPTS_REMAINING");
        int score = extras.getInt("SCORE");

        scoreResultsText = findViewById(R.id.scoreResultsText);
        attemptsResultsText = findViewById(R.id.attemptsResultsText);

        scoreResultsText.setText(String.valueOf(score));
        attemptsResultsText.setText(String.valueOf(attemptsRemaining));
    }

    public void submitScoreBtn(View view) throws FileNotFoundException {

        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            //Toast.makeText(ResultsActivity.this,"Permission to write is granted.",Toast.LENGTH_LONG).show();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(R.string.input_name_message);
            LayoutInflater inflater = this.getLayoutInflater();
            final View mView = inflater.inflate(R.layout.input_name, null);
            alertDialogBuilder.setView(mView);
            alertDialogBuilder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    inputName = (EditText)mView.findViewById(R.id.input_name_field);
                    inputNameString = inputName.getText().toString();
                    System.out.println(inputNameString);

                    if(inputNameString.replaceAll("\\s", "").length() < 1) {
                        Toast.makeText(ResultsActivity.this,R.string.input_name_toast_warning,Toast.LENGTH_LONG).show();
                    }
                    else {
                        try {
                            writeScoreToInternalStorage();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
        else  {

            Toast.makeText(ResultsActivity.this,"In order to store your score on your phone, the app needs the ability to write to your phone's storage.",Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUESTED_PERMISSION);
        }
    }




    public void writeScoreToInternalStorage() throws FileNotFoundException {
        String fileName = "MBscores.txt";
        String seperator = ";";
        String name = inputNameString;
        String score = scoreResultsText.getText().toString();
        String attempts = attemptsResultsText.getText().toString() + "\n";
        File directory = new File(this.getFilesDir().getPath());
        File file = new File(directory, fileName);

        if(!file.exists()) {
            try {
                System.out.println("CURRENT PATH: " + file.getAbsolutePath());

                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream fstream = new FileOutputStream(file, true);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fstream);
            outputWriter.append(name).append(seperator).append(score).append(seperator).append(attempts);

            outputWriter.flush();
            outputWriter.close();

            System.out.println("SAVED TO DIR: " + file.getAbsolutePath());
            Toast.makeText(ResultsActivity.this,"Your score has been saved " + name,Toast.LENGTH_LONG).show();

        }  catch (IOException e) {
            e.printStackTrace();
        }
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
    public void onBackPressed() {
        showNotification = false;
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
    }
}