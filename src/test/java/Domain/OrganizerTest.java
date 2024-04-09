package Domain;

import Domain.Organizer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OrganizerTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        String expectedUsername = "testUser";
        String expectedPassword = "testPassword";

        // Act
        Organizer organizer = new Organizer(expectedUsername, expectedPassword);

        // Assert
        assertEquals(expectedUsername, organizer.getUsername());
        assertEquals(expectedPassword, organizer.getPassword());
    }
}