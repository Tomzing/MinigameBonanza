package hiof.prosjekt.minigamebonanza;

import org.junit.Test;

import hiof.prosjekt.minigamebonanza.ui.main.activities.MinigameLocationActivity;
import hiof.prosjekt.minigamebonanza.ui.main.activities.MinigameShakeActivity;
import hiof.prosjekt.minigamebonanza.ui.main.activities.MinigameStarActivity;

import static org.junit.Assert.*;
public class MinigameTest {

    // Star minigame logic
    @Test
    public void validateStarsPressedIsTrueTest() {
        MinigameStarActivity mock = new MinigameStarActivity();
        assertTrue(mock.hasPlayerPressedFiveStars(5, true));
    }

    @Test
    public void validateStarsPressedIsFalseTest() {
        MinigameStarActivity mock = new MinigameStarActivity();
        assertFalse(mock.hasPlayerPressedFiveStars(4, true));
        assertFalse(mock.hasPlayerPressedFiveStars(1336, true));
    }

    // Shake minigame logic
    @Test
    public void validateShakesIsTrueTest() {
        MinigameShakeActivity mock = new MinigameShakeActivity();
        assertTrue(mock.validateShakes(10, true));
        assertTrue(mock.validateShakes(1338, true));
    }

    @Test
    public void validateShakesIsFalseTest() {
        MinigameShakeActivity mock = new MinigameShakeActivity();
        assertFalse(mock.validateShakes(1, true));
        assertFalse(mock.validateShakes(9, true));
    }

    // Location minigame logic
    @Test
    public void validateLocationPickedIsTrueTest() {
        MinigameLocationActivity mock = new MinigameLocationActivity();
        assertTrue(mock.verifyCorrectCountryCode("NO","NO"));
        assertTrue(mock.verifyCorrectCountryCode("SE","SE"));
    }

    @Test
    public void validateLocationPickedIsFalseTest() {
        MinigameLocationActivity mock = new MinigameLocationActivity();
        assertFalse(mock.verifyCorrectCountryCode("NO","SE"));
        assertFalse(mock.verifyCorrectCountryCode("This is a random sentence","Another random sentence"));
    }
}
