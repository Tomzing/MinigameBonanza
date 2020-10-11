package hiof.prosjekt.minigamebonanza.data.model;

public class Statusbar {
    int attemptsRemaining;
    int score;

    public Statusbar(int attemptsRemaining, int score) {
        this.attemptsRemaining = attemptsRemaining;
        this.score = score;
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
}
