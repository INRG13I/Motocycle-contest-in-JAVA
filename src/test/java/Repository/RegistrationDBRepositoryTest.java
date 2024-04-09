package Repository;

import Domain.Participant;
import Domain.Race;
import Domain.Registration;
import Repository.RaceRepository.RaceDBRepository;
import Repository.RegistrationRepository.RegistrationDBRepository;
import Utils.JdbcUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import static org.junit.Assert.*;

public class RegistrationDBRepositoryTest {

    private Connection connection;
    private RegistrationDBRepository registrationRepository;

    @Before
    public void setUp() throws SQLException {
        Properties props = new Properties();
        try {
            props.load(new FileReader("/Users/lnrg13l/Motocycle_contest/test_db.config"));
        } catch (IOException e) {
            System.out.println("Cannot find test_bd.config " + e);
        }
        registrationRepository = new RegistrationDBRepository(props);
        JdbcUtils dbUtils = new JdbcUtils(props);
        connection = dbUtils.getConnection();

        String createRaceTableSQL = "CREATE TABLE IF NOT EXISTS Race (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "capacity INTEGER NOT NULL)";
        String createParticipantTableSQL = "CREATE TABLE IF NOT EXISTS Participant (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "capacity INTEGER NOT NULL," +
                "team TEXT)";
        String createRegistrationTableSQL = "CREATE TABLE IF NOT EXISTS Registration (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "race_id INTEGER NOT NULL," +
                "participant_id INTEGER NOT NULL," +
                "FOREIGN KEY (race_id) REFERENCES Race(id) ON DELETE CASCADE," +
                "FOREIGN KEY (participant_id) REFERENCES Participant(id) ON DELETE CASCADE)";


        try (PreparedStatement createTableStatement = connection.prepareStatement(createRaceTableSQL)) {
            createTableStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement createTableStatement = connection.prepareStatement(createParticipantTableSQL)) {
            createTableStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement createTableStatement = connection.prepareStatement(createRegistrationTableSQL)) {
            createTableStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws SQLException {
        String dropRaceTableSQL = "DROP TABLE IF EXISTS Race";
        String dropParticipantTableSQL = "DROP TABLE IF EXISTS Participant";
        String dropRegistrationTableSQL = "DROP TABLE IF EXISTS Registration";

        try (PreparedStatement dropTableStatement = connection.prepareStatement(dropRaceTableSQL)) {
            dropTableStatement.executeUpdate();
        }

        try (PreparedStatement dropTableStatement = connection.prepareStatement(dropParticipantTableSQL)) {
            dropTableStatement.executeUpdate();
        }

        try (PreparedStatement dropTableStatement = connection.prepareStatement(dropRegistrationTableSQL)) {
            dropTableStatement.executeUpdate();
        }

        connection.close();
    }

    @Test
    public void testAddRegistration() throws SQLException {
        // Arrange
        Race race = new Race(100);
        race.setId(1);
        addRace(race);
        Participant participant = new Participant("John Doe", 90, "Team A");
        participant.setId(1);
        addParticipant(participant);
        Registration registration = new Registration(race, participant);
        registration.setId(1);

        // Act
        registrationRepository.add(registration);

        // Assert
        Registration retrievedRegistration = registrationRepository.findById(registration.getId());
        assertNotNull(retrievedRegistration);
        assertEquals(race.getId(), retrievedRegistration.getRace().getId());
        assertEquals(participant.getId(), retrievedRegistration.getParticipant().getId());
    }

    @Test
    public void testDeleteRegistration() throws SQLException {
        // Arrange
        Race race = new Race(100);
        race.setId(1);
        addRace(race);
        Participant participant = new Participant("John Doe", 90, "Team A");
        participant.setId(1);
        addParticipant(participant);
        Registration registration = new Registration(race, participant);
        registration.setId(1);

        // Act
        registrationRepository.delete(registration);

        // Assert
        Registration retrievedRegistration = registrationRepository.findById(registration.getId());
        assertNull(retrievedRegistration);
    }

    @Test
    public void testUpdateRegistration() throws SQLException {
        // Arrange
        Race race = new Race(100);
        race.setId(1);
        addRace(race);
        Participant participant = new Participant("John Doe", 90, "Team A");
        participant.setId(1);
        addParticipant(participant);
        Registration registration = new Registration(race, participant);
        registration.setId(1);
        registrationRepository.add(registration);
        Race newRace = new Race(200);
        newRace.setId(2);
        addRace(newRace);
        registration.setRace(newRace);

        // Act
        registrationRepository.update(registration, 1);

        // Assert
        Registration retrievedRegistration = registrationRepository.findById(registration.getId());
        assertNotNull(retrievedRegistration);
        assertEquals(newRace.getId(), retrievedRegistration.getRace().getId());
    }

    @Test
    public void testFindById() throws SQLException {
        // Arrange
        Race race = new Race(100);
        race.setId(1);
        addRace(race);
        Participant participant = new Participant("John Doe", 90, "Team A");
        participant.setId(1);
        addParticipant(participant);
        Registration registration = new Registration(race, participant);
        registration.setId(1);
        registrationRepository.add(registration);

        // Act
        Registration retrievedRegistration = registrationRepository.findById(registration.getId());

        // Assert
        assertNotNull(retrievedRegistration);
        assertEquals(race.getId(), retrievedRegistration.getRace().getId());
        assertEquals(participant.getId(), retrievedRegistration.getParticipant().getId());
    }

    @Test
    public void testFindAll() throws SQLException {
        // Arrange
        Race race1 = new Race(100);
        race1.setId(1);
        addRace(race1);
        Participant participant1 = new Participant("John Doe", 90, "Team A");
        participant1.setId(1);
        addParticipant(participant1);
        Registration registration1 = new Registration(race1, participant1);
        registration1.setId(1);
        registrationRepository.add(registration1);

        Race race2 = new Race(200);
        addRace(race2);
        race2.setId(2);
        Participant participant2 = new Participant("Jane Smith", 150, "Team B");
        addParticipant(participant2);
        participant2.setId(2);
        Registration registration2 = new Registration(race2, participant2);
        registration2.setId(2);
        registrationRepository.add(registration2);

        // Act
        Iterable<Registration> registrations = registrationRepository.findAll();

        // Assert
        int count = 0;
        for (Registration registration : registrations) {
            count++;
        }
        assertEquals(2, count);
    }

    // Helper methods for test setup and assertions

    private void addRace(Race race) throws SQLException {
        String insertSQL = "INSERT INTO Race (capacity) VALUES (?)";
        int count=0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, race.getCapacity());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                count++;
                race.setId(generatedKeys.getInt(count));
            }
        }
    }

    private void addParticipant(Participant participant) throws SQLException {
        String insertSQL = "INSERT INTO Participant (name, capacity, team) VALUES (?, ?, ?)";
        int count =0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, participant.getName());
            preparedStatement.setInt(2, participant.getCapacity());
            preparedStatement.setString(3, participant.getTeam());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                count++;
                participant.setId(generatedKeys.getInt(count));
            }
        }
    }

    private void addRegistration(Registration registration) throws SQLException {
        String insertSQL = "INSERT INTO Registration (race_id, participant_id) VALUES (?, ?)";
        int count =0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, registration.getRace().getId());
            preparedStatement.setInt(2, registration.getParticipant().getId());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                count++;
                registration.setId(generatedKeys.getInt(count));
            }
        }
    }
}

