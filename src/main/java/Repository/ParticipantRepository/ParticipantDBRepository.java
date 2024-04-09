package Repository.ParticipantRepository;

import Domain.Organizer;
import Domain.Participant;
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

public class ParticipantDBRepository implements IParticipantRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public ParticipantDBRepository(Properties props) {
        logger.info("Initializing ParticipantDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public void add(Participant elem) {
        logger.traceEntry("saving task{} ",elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt=con.prepareStatement( "insert into Participant (name, capacity, team) values (?,?,?)")){
            preStmt.setString(  1, elem.getName());
            preStmt.setInt( 2,elem.getCapacity());
            preStmt.setString(3, elem.getTeam());
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Participant elem) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Participant where id = ?")) {
            preStmt.setInt(1, elem.getId());
            int rowsDeleted = preStmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Participant deleted successfully: " + elem);
            } else {
                logger.warn("Participant with ID " + elem.getId() + " not found in the database.");
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Participant participant,Integer id) {
        logger.traceEntry("Updating participant with ID: {}", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Participant set name = ?, capacity = ?, team = ? where id = ?")) {
            preStmt.setString(1, participant.getName());
            preStmt.setInt(2, participant.getCapacity());
            preStmt.setString(3, participant.getTeam());
            preStmt.setInt(4, id);
            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public Participant findById(Integer id){
        logger.traceEntry("Finding participant by ID: {}", id);
        Participant participant = null;
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Participant where id = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    int participantId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int capacity = resultSet.getInt("capacity");
                    String team = resultSet.getString("team");
                    participant = new Participant(name, capacity, team);
                    participant.setId(participantId);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(participant);
        return participant;
    }

    @Override
    public Iterable<Participant> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Participant> participants = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Participant")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    int capacity = result.getInt("capacity");
                    String team =  result.getString("team");
                    Participant participant = new Participant(name, capacity, team);
                    participant.setId(id);
                    participants.add(participant);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
        return participants;

    }

    @Override
    public Collection<Participant> getAll() {
        Iterable<Participant> participants = findAll();
        return (Collection<Participant>) participants;

    }


    public Participant findParticipantByNameCapacityAndTeam(String name, Integer capacity, String team) {
        logger.traceEntry("Finding participant by name: {}, capacity: {}, and team: {}", name, capacity, team);
        Participant participant = null;
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Participant WHERE name = ? AND capacity = ? AND team = ?")) {
            preStmt.setString(1, name);
            preStmt.setInt(2, capacity);
            preStmt.setString(3, team);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    int participantId = resultSet.getInt("id");
                    participant = new Participant(name, capacity, team);
                    participant.setId(participantId);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding participant by name, capacity, and team: ", e);
        }
        logger.traceExit(participant);
        return participant;
    }
}

