package hiof.prosjekt.minigamebonanza;

import org.junit.Test;

import hiof.prosjekt.minigamebonanza.ui.main.utility.MinigameUtility;

import static hiof.prosjekt.minigamebonanza.ui.main.utility.MinigameUtility.calculatePoints;
import static org.junit.Assert.assertEquals;

public class MinigamePointsTest {

    @Test
    public void minigamePointsCalculationTest() {
        assertEquals(400, calculatePoints(10,2));
        assertEquals(100, calculatePoints(1337,0));
        assertEquals(1300, calculatePoints(1337,12));
    }
}
