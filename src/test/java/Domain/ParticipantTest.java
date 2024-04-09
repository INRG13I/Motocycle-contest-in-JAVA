package Domain;

import Domain.Participant;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParticipantTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        String expectedName = "John Doe";
        int expectedCapacity = 10;
        String expectedTeam = "Team A";

        // Act
        Participant participant = new Participant(expectedName, expectedCapacity, expectedTeam);

        // Assert
        assertEquals(expectedName, participant.getName());
        assertEquals(expectedCapacity, participant.getCapacity().intValue());
        assertEquals(expectedTeam, participant.getTeam());
    }
}