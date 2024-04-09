package UI;


import Domain.Participant;
import Domain.Race;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import Service.Service;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class AppController {

    @FXML
    private Label organizerLabel;

    @FXML
    private Button participantsButton;

    @FXML
    private Button racesButton;

    @FXML
    private Button registrationsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private ListView<String> participantsListView;

    @FXML
    private ListView<String> racesListView;

    @FXML
    private ListView<String> teamParticipantsListView;

    @FXML
    private TextField teamTextField;

    @FXML
    private Button findButton;

    @FXML
    private ListView<String> requiredParticipantsListView;

    @FXML
    private BorderPane borderPane;

    private Service service;
    private String username;

    public void setService(Service service) {
        this.service = service;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    void initialize(Service service, String username) {
        // Set up organizer label


        organizerLabel.setText("Logged in as: " + username);
        setService(service);
        setUsername(username);


        // Set up buttons
        participantsButton.setOnAction(event -> {
            try {
                openParticipantsController();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to open participant window.");
            }
        });
        racesButton.setOnAction(event -> {
            try {
                openRacesController();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to open race window.");
            }
        });
        registrationsButton.setOnAction(event -> {
            try {
                openRegistrationsController();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to open registration window.");
            }
        });
        findButton.setOnAction(event -> findParticipants());
        logoutButton.setOnAction(event -> {
            try {
                logout();
            } catch (IOException ex) {

                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to go back.");
            }
        });

        // Load data
        loadParticipants();
        loadRaces();
    }

    private void loadParticipants() {
        participantsListView.getItems().clear(); // Clear the list before loading new data
        Iterable<Participant> participants = service.findAllParticipant();
        for (Participant participant : participants) {
            participantsListView.getItems().add(participant.toString());
        }
    }

    private void loadRaces() {
        racesListView.getItems().clear(); // Clear the list before loading new data
        Map<String, Integer> races = service.getNumberOfParticipantsByRace();
        for (Map.Entry<String, Integer> entry : races.entrySet()) {
            racesListView.getItems().add(entry.getKey() + " - Participants: " + entry.getValue());
        }
    }

    private void findParticipants() {
        String team = teamTextField.getText();
        if (!team.isEmpty()) {
            StringBuilder teamParticipants = service.getTeamParticipants(team);
            if (teamParticipants.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Team does not exist.");
            } else
                teamParticipantsListView.getItems().clear(); // Clear the list before displaying new data
            teamParticipantsListView.getItems().add(teamParticipants.toString());
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a team name.");
        }
    }

    private void openParticipantsController() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/participants.fxml"));
            Parent root = loader.load();
            ParticipantsController participantsController = loader.getController();
            participantsController.initialize(service, username);


            Stage stage = new Stage();
            stage.setTitle("Participants");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the login window
            Stage participanStage = (Stage) participantsButton.getScene().getWindow();
            participanStage.close();
        } catch (Exception e) {
        }
    }

    private void openRacesController() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/races.fxml"));
            Parent root = loader.load();
            RaceController raceController = loader.getController();
            raceController.initialize(service, username);


            Stage stage = new Stage();
            stage.setTitle("Races");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the login window
            Stage raceStage = (Stage) racesButton.getScene().getWindow();
            raceStage.close();
        } catch (Exception e) {
        }
    }

    private void openRegistrationsController() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/registrations.fxml"));
            Parent root = loader.load();
            RegistrationController registrationController = loader.getController();
            registrationController.initialize(service, username);


            Stage stage = new Stage();
            stage.setTitle("Registrations");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the login window
            Stage registrationStage = (Stage) registrationsButton.getScene().getWindow();
            registrationStage.close();
        } catch (Exception e) {
        }
    }

    private void logout() throws IOException {
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();
        loginController.initialize(service);


        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
        stage.show();

        // Close the login window
        Stage loginStage = (Stage) logoutButton.getScene().getWindow();
        loginStage.close();
    } catch(
    Exception e)
    {
    }

}

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Implement other methods as needed

}


