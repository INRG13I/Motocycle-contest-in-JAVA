package Repository;

import Domain.Organizer;
import Repository.OrganizerRepository.OrganizerDBRepository;
import Utils.JdbcUtils;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class OrganizerDBRepositoryTest {

    private Connection connection;
    private OrganizerDBRepository organizerRepository;

    @Before
    public void setUp() throws SQLException {
        Properties props = new Properties();
        try {
            props.load(new FileReader("/Users/lnrg13l/Motocycle_contest/test_db.config"));
        } catch (IOException e) {
            System.out.println("Cannot find test_bd.config " + e);
        }
        organizerRepository = new OrganizerDBRepository(props);
        JdbcUtils dbUtils = new JdbcUtils(props);
        connection = dbUtils.getConnection();

        String createTableQuery = "CREATE TABLE IF NOT EXISTS Organizer (id INTEGER PRIMARY KEY, username VARCHAR(30), password VARCHAR(30))";

        try (PreparedStatement createTableStatement = connection.prepareStatement(createTableQuery)) {
            createTableStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws SQLException {
        String dropTableQuery = "DROP TABLE IF EXISTS Organizer";
        try (PreparedStatement dropTableStatement = connection.prepareStatement(dropTableQuery)) {
            dropTableStatement.executeUpdate();
        }

        connection.close();
    }

    @Test
    public void testAddOrganizer() throws SQLException, IOException {
        Properties props = new Properties();
        props.load(new FileReader("test_db.config"));
        OrganizerDBRepository organizerRepository = new OrganizerDBRepository(props);
        // Arrange
        Organizer organizer = new Organizer("test_username", "test_password");
        organizer.setId(1);

        // Act
        organizerRepository.add(organizer);

        // Assert
        Organizer retrievedOrganizer = findById(organizer.getId());
        assertNotNull(retrievedOrganizer);
        assertEquals(organizer.getUsername(), retrievedOrganizer.getUsername());
        assertEquals(organizer.getPassword(), retrievedOrganizer.getPassword());
    }

    @Test
    public void testDeleteOrganizer() throws SQLException {
        // Arrange
        Organizer organizer = new Organizer("test_username", "test_password");
        addOrganizer(organizer);

        // Act
        organizerRepository.delete(organizer);

        // Assert
        Organizer retrievedOrganizer = findById(organizer.getId());
        assertNull(retrievedOrganizer);
    }

    @Test
    public void testUpdateOrganizer() throws SQLException {
        // Arrange
        Organizer organizer = new Organizer("test_username", "test_password");
        addOrganizer(organizer);
        organizer.setUsername("updated_username");
        organizer.setPassword("updated_password");

        // Act
        organizerRepository.update(organizer, organizer.getId());

        // Assert
        Organizer retrievedOrganizer = findById(organizer.getId());
        assertNotNull(retrievedOrganizer);
        assertEquals("updated_username", retrievedOrganizer.getUsername());
        assertEquals("updated_password", retrievedOrganizer.getPassword());
    }

    @Test
    public void testFindById() throws SQLException {
        // Arrange
        Organizer organizer = new Organizer("test_username", "test_password");
        addOrganizer(organizer);

        // Act
        Organizer retrievedOrganizer = organizerRepository.findById(organizer.getId());

        // Assert
        assertNotNull(retrievedOrganizer);
        assertEquals(organizer.getUsername(), retrievedOrganizer.getUsername());
        assertEquals(organizer.getPassword(), retrievedOrganizer.getPassword());
    }

    @Test
    public void testFindAll() throws SQLException {
        // Arrange
        addOrganizer(new Organizer("user1", "pass1"));
        addOrganizer(new Organizer("user2", "pass2"));

        // Act
        Iterable<Organizer> organizers = organizerRepository.findAll();

        // Assert
        int count = 0;
        for (Organizer organizer : organizers) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testOrganizerFindByUsernameAndPassword() throws SQLException {
        // Arrange
        Organizer organizer = new Organizer("test_username", "test_password");
        addOrganizer(organizer);

        // Act
        Organizer retrievedOrganizer = organizerRepository.findOrganizerByUsernameAndPassword("test_username", "test_password");

        // Assert
        assertNotNull(retrievedOrganizer);
        assertEquals(organizer.getUsername(), retrievedOrganizer.getUsername());
        assertEquals(organizer.getPassword(), retrievedOrganizer.getPassword());
    }

    // Helper methods for test setup and assertions

    private void createTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Organizer (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL)";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
        }
    }

    private void dropTable() throws SQLException {
        String dropTableSQL = "DROP TABLE IF EXISTS Organizer";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(dropTableSQL);
        }
    }

    private void addOrganizer(Organizer organizer) throws SQLException {
        String insertSQL = "INSERT INTO Organizer (username, password) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, organizer.getUsername());
            preparedStatement.setString(2, organizer.getPassword());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                organizer.setId(generatedKeys.getInt(1));
            }
        }
    }

    private Organizer findById(int id) throws SQLException {
        String selectSQL = "SELECT * FROM Organizer WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    Organizer organizer = new Organizer(username, password);
                    organizer.setId(id);
                    return organizer;
                }
            }
        }
        return null;
    }
}
