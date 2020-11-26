package hiof.prosjekt.minigamebonanza;

import org.junit.Test;

import hiof.prosjekt.minigamebonanza.ui.main.activities.MinigameShakeActivity;
import hiof.prosjekt.minigamebonanza.ui.main.activities.MinigameStarActivity;

import static org.junit.Assert.*;
public class MinigameTest {

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
}
