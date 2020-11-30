package hiof.prosjekt.minigamebonanza.data.model;

import androidx.annotation.NonNull;

public class PlayerScore {
    String name;
    int attemptsRemaining;
    int score;

    public PlayerScore(String name, int attemptsRemaining, int score) {
        this.name = name;
        this.attemptsRemaining = attemptsRemaining;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttemptsRemaining() {
        return attemptsRemaining;
    }

    public void setAttemptsRemaining(int attemptsRemaining) {
        this.attemptsRemaining = attemptsRemaining;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    @NonNull
    @Override
    public String toString() {
        return this.getName() + " " + this.getAttemptsRemaining() + " " + this.getScore();
    }

}
