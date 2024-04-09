package Repository.RaceRepository;

import Domain.Participant;
import Domain.Race;
import Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class RaceDBRepository implements IRaceRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger(RaceDBRepository.class);

    public RaceDBRepository(Properties props) {
        logger.info("Initializing RaceDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Race race) {
        logger.traceEntry("Saving race: {}", race);
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("INSERT INTO Race (capacity) VALUES (?)")) {
            preStmt.setInt(1, race.getCapacity());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error("Error adding race: ", ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Race race) {
        logger.traceEntry();
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("DELETE FROM Race WHERE id = ?")) {
            preStmt.setInt(1, race.getId());
            int rowsDeleted = preStmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Race deleted successfully: " + race);
            } else {
                logger.warn("Race with ID " + race.getId() + " not found in the database.");
            }
        } catch (SQLException e) {
            logger.error("Error deleting race: ", e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Race race, Integer id) {
        logger.traceEntry("Updating race with ID: {}", id);
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("UPDATE Race SET capacity = ? WHERE id = ?")) {
            preStmt.setInt(1, race.getCapacity());
            preStmt.setInt(2, id);
            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException ex) {
            logger.error("Error updating race: ", ex);
        }
        logger.traceExit();
    }

    @Override
    public Race findById(Integer id) {
        logger.traceEntry("Finding race by ID: {}", id);
        Race race = null;
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Race WHERE id = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    int raceId = resultSet.getInt("id");
                    int capacity = resultSet.getInt("capacity");
                    race = new Race(capacity);
                    race.setId(raceId);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding race by ID: ", e);
        }
        logger.traceExit(race);
        return race;
    }

    @Override
    public Iterable<Race> findAll() {
        logger.traceEntry();
        List<Race> races = new ArrayList<>();
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Race");
             ResultSet result = preStmt.executeQuery()) {
            while (result.next()) {
                int id = result.getInt("id");
                int capacity = result.getInt("capacity");
                Race race = new Race(capacity);
                race.setId(id);
                races.add(race);
            }
        } catch (SQLException e) {
            logger.error("Error finding all races: ", e);
        }
        logger.traceExit();
        return races;
    }

    @Override
    public Collection<Race> getAll() {
        Iterable<Race> races = findAll();
        return (Collection<Race>) races;
    }

    @Override
    public Race findRaceByCapacity(Integer capacity) {
        logger.traceEntry("Finding race by capacity: {}", capacity);
        Race race = null;
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Race WHERE capacity = ?")) {
            preStmt.setInt(1, capacity);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    int raceId = resultSet.getInt("id");
                    race = new Race(capacity);
                    race.setId(raceId);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding race by capacity: ", e);
        }
        logger.traceExit(race);
        return race;
    }
}

