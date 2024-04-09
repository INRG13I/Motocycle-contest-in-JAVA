import Repository.OrganizerRepository.IOrganiserRepository;
import Repository.OrganizerRepository.OrganizerDBRepository;
import Repository.ParticipantRepository.IParticipantRepository;
import Repository.ParticipantRepository.ParticipantDBRepository;
import Repository.RaceRepository.IRaceRepository;
import Repository.RaceRepository.RaceDBRepository;
import Repository.RegistrationRepository.IRegistrationRepository;
import Repository.RegistrationRepository.RegistrationDBRepository;
import Service.Service;
import UI.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

//todo if you delete it and what to clone it back put in the home directory
//thank me later

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Properties props = new Properties();
        try {
            props.load(new FileReader("main_bd.config"));
            System.out.println("JDBC PENTRU SQLITE DB");
        } catch (IOException e) {
            System.out.println("Cannot find main_bd.config " + e);
        }
        System.out.println("luam din bd.config " + props.getProperty("jdbc.url"));


        IOrganiserRepository organiserRepository = new OrganizerDBRepository(props);
        IParticipantRepository participantRepository = new ParticipantDBRepository(props);
        IRaceRepository raceRepository = new RaceDBRepository(props);
        IRegistrationRepository registrationRepository = new RegistrationDBRepository(props);

        Service service = new Service(participantRepository, organiserRepository, raceRepository , registrationRepository);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();
        loginController.initialize(service);


        stage.setScene(new Scene(root));
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}