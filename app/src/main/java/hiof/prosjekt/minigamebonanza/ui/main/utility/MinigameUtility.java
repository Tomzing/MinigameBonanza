package hiof.prosjekt.minigamebonanza.ui.main.utility;

public abstract class MinigameUtility {
    public static int calculatePoints(int timeAvailable, int timeSpent) {
        System.out.println("Minigame time: " + timeAvailable + ". Time spent upon completion: " + timeSpent);
        //System.out.println("Calculate points returns " + (timeAvailable + timeSpent)  * 2);
        if(timeSpent == 0) {
            System.out.println("Calculate points returns " + 100 + " since you completed at 0 seconds remaining");
            return 100;
        }
        else {
            System.out.println("Calculate points return " + (timeAvailable / (timeAvailable - timeSpent)) * 2);
            return (timeAvailable / (timeAvailable - timeSpent)) * 100;
        }
        //return (timeAvailable + timeSpent)  * 5;

    }
}
