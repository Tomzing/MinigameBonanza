package hiof.prosjekt.minigamebonanza.ui.main.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import hiof.prosjekt.minigamebonanza.R;
import hiof.prosjekt.minigamebonanza.data.model.PlayerScore;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        try {
            initLeaderboardView();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initLeaderboardView() throws FileNotFoundException {
        ListView listView = (ListView) findViewById(R.id.leaderboardListView);

        String fileName = "MBscores.txt";
        File directory = new File(this.getFilesDir().getPath());
        //String directory = "/data/data/hiof.prosjekt.minigamebonanza/files/";
        File file = new File(directory, fileName);
        String[] headerList = {"Name","Attempts remaining","Score"};

        // If file exists, get contents
        if(file.exists()) {
            ArrayList<PlayerScore> list = new ArrayList<PlayerScore>();
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] splitLine = line.split(";");

                PlayerScore tempPlayer = new PlayerScore(splitLine[0],Integer.parseInt(splitLine[2]),Integer.parseInt(splitLine[1]));
                list.add(tempPlayer);
            }
            ArrayAdapter<String> headerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, headerList);
            ArrayAdapter<PlayerScore> playerScoresAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

            listView.setAdapter(headerAdapter);
            listView.setAdapter(playerScoresAdapter);


        }
        // Else if the MBscores file doesn't exist, populate with dummy scores
        else {
            ArrayAdapter<String> headerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, headerList);

            ArrayList<PlayerScore> playerScoreList = new ArrayList<PlayerScore>();
            PlayerScore player1 = new PlayerScore("Bob",2,500);
            PlayerScore player2 = new PlayerScore("Bort",3,1000);
            PlayerScore player3 = new PlayerScore("Ida",3,1337);
            PlayerScore[] playerScoreArray = {player1,player2,player3};
            playerScoreList.add(player1);
            playerScoreList.add(player2);
            playerScoreList.add(player3);

            ArrayAdapter<PlayerScore> playerScoresAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,playerScoreArray);

            listView.setAdapter(headerAdapter);
            listView.setAdapter(playerScoresAdapter);
        }
    }
}