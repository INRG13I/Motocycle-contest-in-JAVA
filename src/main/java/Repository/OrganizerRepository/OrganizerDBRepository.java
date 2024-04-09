package Repository.OrganizerRepository;

import Domain.Organizer;
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

public class OrganizerDBRepository implements IOrganiserRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public OrganizerDBRepository(Properties props) {
        logger.info("Initializing OrganizerDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public void add(Organizer elem) {
        logger.traceEntry("saving task{} ",elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt=con.prepareStatement( "insert into Organizer (username, password) values (?,?)")){
            preStmt.setString(1, elem.getUsername());
            preStmt.setString(2,elem.getPassword());
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Organizer elem) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Organizer where id = ?")) {
            preStmt.setInt(1, elem.getId());
            int rowsDeleted = preStmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Organizer deleted successfully: " + elem);
            } else {
                logger.warn("Organizer with ID " + elem.getId() + " not found in the database.");
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Organizer organizer,Integer id) {
        logger.traceEntry("Updating organizer with ID: {}", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Organizer set username = ?, password = ? where id = ?")) {
            preStmt.setString(1, organizer.getUsername());
            preStmt.setString(2, organizer.getPassword());
            preStmt.setInt(3, id);
            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public Organizer findById(Integer id){
        logger.traceEntry("Finding organizer by ID: {}", id);
        Organizer organizer = null;
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Organizer where id = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    organizer = new Organizer(username, password);
                    organizer.setId(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(organizer);
        return organizer;
    }

    @Override
    public Iterable<Organizer> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Organizer> organizers = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Organizer")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    Organizer organizer = new Organizer(username, password);
                    organizer.setId(id);
                    organizers.add(organizer);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
        return organizers;
    }

    @Override
    public Collection<Organizer> getAll() {
        Iterable<Organizer> organizers = findAll();
        return (Collection<Organizer>) organizers;
    }

    @Override
    public Organizer findOrganizerByUsernameAndPassword(String username, String password) {
        logger.traceEntry("Finding organizer by username and password: {}", username);
        Organizer organizer = null;
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Organizer WHERE username = ? AND password = ?")) {
            preStmt.setString(1, username);
            preStmt.setString(2, password);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    int organizerId = resultSet.getInt("id");
                    String organizerUsername = resultSet.getString("username");
                    String organizerPassword = resultSet.getString("password");
                    organizer = new Organizer(organizerUsername, organizerPassword);
                    organizer.setId(organizerId);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding organizer by username and password: ", e);
        }
        logger.traceExit(organizer);
        return organizer;
    }
}
