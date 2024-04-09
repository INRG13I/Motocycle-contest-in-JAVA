package Repository;

import Domain.Participant;
import Repository.OrganizerRepository.OrganizerDBRepository;
import Repository.ParticipantRepository.ParticipantDBRepository;
import Utils.JdbcUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import static org.junit.Assert.*;

public class ParticipantDBRepositoryTest {

    private Connection connection;
    private ParticipantDBRepository participantRepository;

    @Before
    public void setUp() throws SQLException {
        Properties props = new Properties();
        try {
            props.load(new FileReader("/Users/lnrg13l/Motocycle_contest/test_db.config"));
        } catch (IOException e) {
            System.out.println("Cannot find test_bd.config " + e);
        }
        participantRepository = new ParticipantDBRepository(props);
        JdbcUtils dbUtils = new JdbcUtils(props);
        connection = dbUtils.getConnection();

        String createTableQuery = "CREATE TABLE IF NOT EXISTS Participant (id INTEGER PRIMARY KEY, name VARCHAR(30), capacity Integer, team VARCHAR(30))";

        try (PreparedStatement createTableStatement = connection.prepareStatement(createTableQuery)) {
            createTableStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws SQLException {
        String dropTableQuery = "DROP TABLE IF EXISTS Participant";
        try (PreparedStatement dropTableStatement = connection.prepareStatement(dropTableQuery)) {
            dropTableStatement.executeUpdate();
        }

        connection.close();
    }

    @Test
    public void testAddParticipant() throws SQLException {
        // Arrange
        Participant participant = new Participant("test_name", 10, "test_team");
        participant.setId(1);

        // Act
        participantRepository.add(participant);

        // Assert
        Participant retrievedParticipant = findById(participant.getId());
        assertNotNull(retrievedParticipant);
        assertEquals(participant.getName(), retrievedParticipant.getName());
        assertEquals(participant.getCapacity(), retrievedParticipant.getCapacity());
        assertEquals(participant.getTeam(), retrievedParticipant.getTeam());
    }

    @Test
    public void testDeleteParticipant() throws SQLException {
        // Arrange
        Participant participant = new Participant("test_name", 10, "test_team");
        addParticipant(participant);

        // Act
        participantRepository.delete(participant);

        // Assert
        Participant retrievedParticipant = findById(participant.getId());
        assertNull(retrievedParticipant);
    }

    @Test
    public void testUpdateParticipant() throws SQLException {
        // Arrange
        Participant participant = new Participant("test_name", 10, "test_team");
        addParticipant(participant);
        participant.setName("updated_name");
        participant.setCapacity(20);
        participant.setTeam("updated_team");

        // Act
        participantRepository.update(participant, participant.getId());

        // Assert
        Participant retrievedParticipant = findById(participant.getId());
        assertNotNull(retrievedParticipant);
        assertEquals("updated_name", retrievedParticipant.getName());
        assertEquals((Integer) 20, retrievedParticipant.getCapacity());
        assertEquals("updated_team", retrievedParticipant.getTeam());
    }

    @Test
    public void testFindById() throws SQLException {
        // Arrange
        Participant participant = new Participant("test_name", 10, "test_team");
        addParticipant(participant);

        // Act
        Participant retrievedParticipant = participantRepository.findById(participant.getId());

        // Assert
        assertNotNull(retrievedParticipant);
        assertEquals(participant.getName(), retrievedParticipant.getName());
        assertEquals(participant.getCapacity(), retrievedParticipant.getCapacity());
        assertEquals(participant.getTeam(), retrievedParticipant.getTeam());
    }

    @Test
    public void testFindAll() throws SQLException {
        // Arrange
        addParticipant(new Participant("name1", 10, "team1"));
        addParticipant(new Participant("name2", 20, "team2"));

        // Act
        Iterable<Participant> participants = participantRepository.findAll();

        // Assert
        int count = 0;
        for (Participant participant : participants) {
            count++;
        }
        assertEquals(2, count);
    }

    private void addParticipant(Participant participant) throws SQLException {
        String insertSQL = "INSERT INTO Participant (name, capacity, team) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, participant.getName());
            preparedStatement.setInt(2, participant.getCapacity());
            preparedStatement.setString(3, participant.getTeam());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                participant.setId(generatedKeys.getInt(1));
            }
        }
    }

    private Participant findById(int id) throws SQLException {
        String selectSQL = "SELECT * FROM Participant WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int capacity = resultSet.getInt("capacity");
                    String team = resultSet.getString("team");
                    Participant participant = new Participant(name, capacity, team);
                    participant.setId(id);
                    return participant;
                }
            }
        }
        return null;
    }
}

