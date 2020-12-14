package hiof.prosjekt.minigamebonanza.ui.main.utility;

public abstract class MinigameUtility {
    public static int calculatePoints(int timeAvailable, int timeRemaining) {
        System.out.println("Minigame time: " + timeAvailable + ". Time spent upon completion: " + timeRemaining);
        int points = 0;
        //System.out.println("Calculate points returns " + (timeAvailable + timeRemaining)  * 2);
        if(timeRemaining == 0) {
            System.out.println("Calculate points returns " + 100 + " since you completed at 0 seconds remaining");
            return 100;
        }
        else {
            if(timeRemaining < 2) {
                points = 200;
            }
            else if(timeRemaining < 4) {
                points = 400;
            }
            else if(timeRemaining < 6) {
                points = 600;
            }
            else if(timeRemaining < 8) {
                points = 800;
            }
            else if(timeRemaining < 9) {
                points = 900;
            }
            else if(timeRemaining < 11) {
                points = 1200;
            }
            else if(timeRemaining < 13) {
                points = 1300;
            }

            System.out.println("Calculate points return " + points);

            return points;
        }
    }
}
