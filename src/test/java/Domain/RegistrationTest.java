package Domain;

import Domain.Participant;
import Domain.Race;
import Domain.Registration;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RegistrationTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        Race expectedRace = new Race(100);
        Participant expectedParticipant = new Participant("John Doe", 10, "Team A");

        // Act
        Registration registration = new Registration(expectedRace, expectedParticipant);

        // Assert
        assertEquals(expectedRace, registration.getRace());
        assertEquals(expectedParticipant, registration.getParticipant());
    }
}
