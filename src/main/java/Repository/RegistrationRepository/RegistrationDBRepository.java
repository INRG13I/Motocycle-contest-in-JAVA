package Repository.RegistrationRepository;

import Domain.Participant;
import Domain.Race;
import Domain.Registration;
import Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class RegistrationDBRepository implements IRegistrationRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger(RegistrationDBRepository.class);

    public RegistrationDBRepository(Properties props) {
        logger.info("Initializing RegistrationDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Registration registration) {
        logger.traceEntry("Saving registration: {}", registration);
        try (Connection con = dbUtils.getConnection()) {
            // Retrieve participant capacity
            int participantCapacity = registration.getParticipant().getCapacity();

            // Retrieve race capacity
            int raceCapacity;
            try (PreparedStatement raceStmt = con.prepareStatement("SELECT capacity FROM Race WHERE id = ?")) {
                raceStmt.setInt(1, registration.getRace().getId());
                try (ResultSet raceResult = raceStmt.executeQuery()) {
                    if (raceResult.next()) {
                        raceCapacity = raceResult.getInt("capacity");
                    } else {
                        logger.error("Error adding registration: Race with ID " + registration.getRace().getId() + " not found.");
                        return;
                    }
                }
            }

            // Check if participant capacity exceeds race capacity
            if (participantCapacity > raceCapacity) {
                logger.error("Error adding registration: Participant capacity exceeds race capacity.");
                return;
            }

            // Proceed with adding registration
            try (PreparedStatement preStmt = con.prepareStatement("INSERT INTO Registration (race_id, participant_id) VALUES (?, ?)")) {
                preStmt.setInt(1, registration.getRace().getId());
                preStmt.setInt(2, registration.getParticipant().getId());
                int result = preStmt.executeUpdate();
                logger.trace("Saved {} instances", result);
            }

        } catch (SQLException ex) {
            logger.error("Error adding registration: ", ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Registration registration) {
        logger.traceEntry();
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("DELETE FROM Registration WHERE id = ?")) {
            preStmt.setInt(1, registration.getId());
            int rowsDeleted = preStmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("RegistrationService deleted successfully: " + registration);
            } else {
                logger.warn("RegistrationService with ID " + registration.getId() + " not found in the database.");
            }
        } catch (SQLException e) {
            logger.error("Error deleting registration: ", e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Registration registration, Integer id) {
        logger.traceEntry("Updating registration with ID: {}", id);
        try (Connection con = dbUtils.getConnection()) {
            // Retrieve participant capacity
            int participantCapacity = registration.getParticipant().getCapacity();

            // Retrieve race capacity
            int raceCapacity;
            try (PreparedStatement raceStmt = con.prepareStatement("SELECT capacity FROM Race WHERE id = ?")) {
                raceStmt.setInt(1, registration.getRace().getId());
                try (ResultSet raceResult = raceStmt.executeQuery()) {
                    if (raceResult.next()) {
                        raceCapacity = raceResult.getInt("capacity");
                    } else {
                        logger.error("Error updating registration: Race with ID " + registration.getRace().getId() + " not found.");
                        return;
                    }
                }
            }

            // Check if participant capacity exceeds race capacity
            if (participantCapacity > raceCapacity) {
                logger.error("Error updating registration: Participant capacity exceeds race capacity.");
                return;
            }

            // Proceed with updating registration
            try (PreparedStatement preStmt = con.prepareStatement("UPDATE Registration SET race_id = ?, participant_id = ? WHERE id = ?")) {
                preStmt.setInt(1, registration.getRace().getId());
                preStmt.setInt(2, registration.getParticipant().getId());
                preStmt.setInt(3, id);
                int result = preStmt.executeUpdate();
                logger.trace("Updated {} instances", result);
            }

        } catch (SQLException ex) {
            logger.error("Error updating registration: ", ex);
        }
        logger.traceExit();
    }

    @Override
    public Registration findById(Integer id) {
        logger.traceEntry("Finding registration by ID: {}", id);
        Registration registration = null;
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Registration WHERE id = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    int registrationId = resultSet.getInt("id");
                    int raceId = resultSet.getInt("race_id");
                    int participantId = resultSet.getInt("participant_id");
                    Race race = getRaceById(raceId, con);
                    race.setId(raceId);
                    Participant participant = getParticipantById(participantId, con);
                    participant.setId(participantId);
                    registration = new Registration(race, participant);
                    registration.setId(id);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding registration by ID: ", e);
        }
        logger.traceExit(registration);
        return registration;
    }

    @Override
    public Iterable<Registration> findAll() {
        logger.traceEntry();
        ArrayList<Registration> registrations = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Registration");
             ResultSet result = preStmt.executeQuery()) {
            while (result.next()) {
                int id = result.getInt("id");
                int raceId = result.getInt("race_id");
                int participantId = result.getInt("participant_id");
                Race race = getRaceById(raceId, con);
                Participant participant = getParticipantById(participantId, con);
                Registration registration = new Registration(race, participant);
                registration.setId(id);
                registrations.add(registration);
            }
        } catch (SQLException e) {
            logger.error("Error finding all registrations.fxml: ", e);
        }
        logger.traceExit();
        return registrations;
    }

    @Override
    public Collection<Registration> getAll() {
        Iterable<Registration> registrations = findAll();
        return (Collection<Registration>) registrations;
    }

    private Race getRaceById(int raceId, Connection con) throws SQLException {
        try(PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Race WHERE id = ?")) {
            preStmt.setInt(1, raceId);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int capacity = resultSet.getInt("capacity");
                    Race newRace = new Race(capacity);
                    newRace.setId(raceId);
                    return newRace;
                }
            }
        }
        return null;
    }

    private Participant getParticipantById(int participantId, Connection con) throws SQLException {
         try(PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Participant WHERE id = ?")) {
            preStmt.setInt(1, participantId);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int capacity = resultSet.getInt("capacity");
                    String team = resultSet.getString("team");
                    Participant newParticipant = new Participant(name, capacity, team);
                    newParticipant.setId(participantId);
                    return newParticipant;
                }
            }
        }
        return null;
    }
}
