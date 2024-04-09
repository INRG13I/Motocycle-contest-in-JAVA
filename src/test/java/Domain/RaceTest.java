package Domain;

import Domain.Race;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RaceTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        int expectedCapacity = 100;

        // Act
        Race race = new Race(expectedCapacity);

        // Assert
        assertEquals(expectedCapacity, race.getCapacity().intValue());
    }
}

