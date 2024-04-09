package Repository;

import Domain.Race;
import Repository.ParticipantRepository.ParticipantDBRepository;
import Repository.RaceRepository.RaceDBRepository;
import Utils.JdbcUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class RaceDBRepositoryTest {

    private Connection connection;
    private RaceDBRepository raceRepository;

    @Before
    public void setUp() throws SQLException {
        Properties props = new Properties();
        try {
            props.load(new FileReader("/Users/lnrg13l/Motocycle_contest/test_db.config"));
        } catch (IOException e) {
            System.out.println("Cannot find test_bd.config " + e);
        }
        raceRepository = new RaceDBRepository(props);
        JdbcUtils dbUtils = new JdbcUtils(props);
        connection = dbUtils.getConnection();

        String createTableQuery = "CREATE TABLE IF NOT EXISTS Race (id INTEGER PRIMARY KEY, capacity Integer)";

        try (PreparedStatement createTableStatement = connection.prepareStatement(createTableQuery)) {
            createTableStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws SQLException {
        String dropTableQuery = "DROP TABLE IF EXISTS Race";
        try (PreparedStatement dropTableStatement = connection.prepareStatement(dropTableQuery)) {
            dropTableStatement.executeUpdate();
        }

        connection.close();
    }

    @Test
    public void testAddRace() throws SQLException {
        // Arrange
        Race race = new Race(100);
        race.setId(1);

        // Act
        raceRepository.add(race);

        // Assert
        Race retrievedRace = findById(race.getId());
        assertNotNull(retrievedRace);
        assertEquals(race.getCapacity(), retrievedRace.getCapacity());
    }

    @Test
    public void testDeleteRace() throws SQLException {
        // Arrange
        Race race = new Race(100);
        addRace(race);

        // Act
        raceRepository.delete(race);

        // Assert
        Race retrievedRace = findById(race.getId());
        assertNull(retrievedRace);
    }

    @Test
    public void testUpdateRace() throws SQLException {
        // Arrange
        Race race = new Race(100);
        addRace(race);
        race.setCapacity(200);

        // Act
        raceRepository.update(race, race.getId());

        // Assert
        Race retrievedRace = findById(race.getId());
        assertNotNull(retrievedRace);
        assertEquals((Integer) 200, retrievedRace.getCapacity());
    }

    @Test
    public void testFindById() throws SQLException {
        // Arrange
        Race race = new Race(100);
        addRace(race);

        // Act
        Race retrievedRace = raceRepository.findById(race.getId());

        // Assert
        assertNotNull(retrievedRace);
        assertEquals(race.getCapacity(), retrievedRace.getCapacity());
    }

    @Test
    public void testFindAll() throws SQLException {
        // Arrange
        addRace(new Race(100));
        addRace(new Race(200));

        // Act
        Iterable<Race> races = raceRepository.findAll();

        // Assert
        int count = 0;
        for (Race race : races) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testFindRaceByCapacity() throws SQLException {
        // Arrange
        Race race = new Race(100);
        race.setId(1);
        addRace(race);

        // Act
        Race retrievedRace = raceRepository.findRaceByCapacity(100);

        // Assert
        assertNotNull(retrievedRace);
        assertEquals(race.getId(), retrievedRace.getId());
        assertEquals(race.getCapacity(), retrievedRace.getCapacity());
    }


    private void addRace(Race race) throws SQLException {
        String insertSQL = "INSERT INTO Race (capacity) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, race.getCapacity());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                race.setId(generatedKeys.getInt(1));
            }
        }
    }

    private Race findById(int id) throws SQLException {
        String selectSQL = "SELECT * FROM Race WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int capacity = resultSet.getInt("capacity");
                    Race race = new Race(capacity);
                    race.setId(id);
                    return race;
                }
            }
        }
        return null;
    }
}

