package hiof.prosjekt.minigamebonanza.ui.main.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static java.util.List.of;

public class ResultsActivity extends AppCompatActivity {

    TextView scoreResultsText, attemptsResultsText;
    int submitBtnActive = 0;
    int REQUESTED_PERMISSION = 1;
    EditText inputName;
    String inputNameString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        /*getSupportFragmentManager().beginTransaction()
                .add(R.id.container, ResultsFragment.newInstance())
                .commitNow();*/

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
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

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            System.out.println("Landscape");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            System.out.println("PORTRAIT");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    public void submitScoreBtn(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.input_name_message);
        LayoutInflater inflater = this.getLayoutInflater();
        final View mView = inflater.inflate(R.layout.input_name, null);
        alertDialogBuilder.setView(mView);
        alertDialogBuilder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(ResultsActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                //writeScoreToInternalStorage();

                inputName = (EditText)mView.findViewById(R.id.input_name_field);
                inputNameString = inputName.getText().toString();
                System.out.println(inputNameString);

                if(inputNameString.replaceAll("\\s", "").length() < 1) {
                    Toast.makeText(ResultsActivity.this,R.string.input_name_toast_warning,Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        requestPermissions();
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




    public void writeScoreToInternalStorage() throws FileNotFoundException {
        String fileName = "MBscores.txt";
        String seperator = ";";
        String name = inputNameString;
        String score = scoreResultsText.getText().toString();
        String attempts = attemptsResultsText.getText().toString() + "\n";
        //File directory = new File(this.getFilesDir() + "/" + "files");
        File directory = new File(this.getFilesDir().getPath());
        //String directory = "/data/data/hiof.prosjekt.minigamebonanza/files/";
        File file = new File(directory, fileName);

        //File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);


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
            //FileWriter writer = new FileWriter(file);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fstream);
            outputWriter.append(name).append(seperator).append(score).append(seperator).append(attempts);

            outputWriter.flush();
            outputWriter.close();

            System.out.println("SAVED TO DIR: " + file.getAbsolutePath());
            Toast.makeText(ResultsActivity.this,"Something has been saved",Toast.LENGTH_LONG).show();

            //FileOutputStream fileOutputStream = new FileOutputStream(file);
            //FileOutputStream.write(inputNameString + seperator + scoreResultsText + seperator + attemptsResultsText)
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });*/

    private void requestPermissions() throws FileNotFoundException {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(ResultsActivity.this,"Permission to write is granted.",Toast.LENGTH_LONG).show();
            writeScoreToInternalStorage();

        }
        else if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {

            Toast.makeText(ResultsActivity.this,"In order to store your score on your phone, the app needs the ability to write to your phone's storage.",Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUESTED_PERMISSION);
        }
    }

    /*public void submitScoreBtn(View v) {

        InputNameDialogFragment dialog = new InputNameDialogFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, InputNameDialogFragment.newInstance())
                .commitNow();
        /*
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        SubmitScoreFragment submitScoreFragment = new SubmitScoreFragment();

        if(submitBtnActive == 0) {
            submitBtnActive = 1;
            fragmentTransaction.add(R.id.container, submitScoreFragment);
        }
        else {
            submitBtnActive = 0;
            fragmentTransaction.remove(submitScoreFragment);
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }*/


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
    }
}