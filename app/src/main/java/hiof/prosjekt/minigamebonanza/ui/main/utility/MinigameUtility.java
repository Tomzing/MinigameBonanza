package hiof.prosjekt.minigamebonanza.ui.main.utility;

public abstract class MinigameUtility {
    public static Float calculatePoints(Float timeSpent, Float timeAvailable) {
        return timeAvailable - timeSpent  * 10;
    }
}
