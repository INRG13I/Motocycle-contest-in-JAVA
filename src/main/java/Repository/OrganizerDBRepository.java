package Repository;

import Domain.Organizer;
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

public class OrganizerDBRepository implements Repository<Organizer,Integer>{

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
        try (PreparedStatement preStmt=con.prepareStatement( "insert into Organizers (username, password) values (?,?)")){

            preStmt.setString(  1, elem.getUsername());
            preStmt.setString( 2,elem.getPassword());
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
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Organizers where id="+elem.getId())){
            preStmt.execute();
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB "+ex);
        }
    }

    @Override
    public Organizer findById(Integer id){
        return null;
    }

    @Override
    public void update(Organizer elem,Integer id) {
      //to do
    }

    @Override
    public Iterable<Organizer> findAll() {

        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Organizer> Organizers=new ArrayList<>();
        try (PreparedStatement preStmt=con.prepareStatement ( "select * from Organizers")) { try(ResultSet result=preStmt.executeQuery()) {
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString(  "username");
                String pass = result.getString( "password");
                Organizer Organizer=new Organizer (id,name, pass);
                Organizer.setId(id);
                Organizers.add(Organizer);
            }
        }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB "+e);
        }

        logger.traceExit (Organizers);
        return Organizers;

    }

    @Override
    public Collection<Organizer> getAll() {

        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Organizer> Organizers=new ArrayList<>();
        try (PreparedStatement preStmt=con.prepareStatement ( "select * from Organizers")) { try(ResultSet result=preStmt.executeQuery()) {
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString(  "username");
                String pass = result.getString( "password");
                Organizer Organizer=new Organizer (id,name, pass);
                Organizer.setId(id);
                Organizers.add(Organizer);
            }
        }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB "+e);
        }

        logger.traceExit (Organizers);
        return Organizers;

    }
}
