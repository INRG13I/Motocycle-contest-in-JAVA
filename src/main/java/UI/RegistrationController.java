package UI;

import Domain.Participant;
import Domain.Race;
import Domain.Registration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import Service.Service;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RegistrationController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private ListView<String> participantsListView;

    @FXML
    private ListView<String> racesListView;

    @FXML
    private ListView<String> registrationsListView;

    @FXML
    private TextField registrationIdTextField;

    @FXML
    private TextField raceIdTextField;

    @FXML
    private TextField participantIdTextField;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button backButton;

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
        setService(service);
        backButton.setOnAction(event -> goBack());
        addButton.setOnAction(event -> addRegistration());
        deleteButton.setOnAction(event -> deleteRegistration());
        updateButton.setOnAction(event -> updateRegistration());
        loadParticipants();
        loadRaces();
        loadRegistrations();
    }

    private void loadParticipants() {
        participantsListView.getItems().clear();
        Iterable<Participant> participants = service.findAllParticipant();
        for (Participant participant : participants) {
            participantsListView.getItems().add(participant.toString());
        }
    }

    private void loadRaces() {
        racesListView.getItems().clear();
        Iterable<Race> races = service.findAllRace();
        for (Race race : races) {
            racesListView.getItems().add(race.toString());
        }
    }

    private void loadRegistrations() {
        registrationsListView.getItems().clear(); // Clear the list before loading new data
        Iterable<Registration> registrations = service.findAllRegistration();
        for (Registration registration : registrations) {
            registrationsListView.getItems().add(registration.toString());
        }
    }


    private void addRegistration() {
        // Retrieve data from text fields
        String raceId = raceIdTextField.getText();
        String participantId = participantIdTextField.getText();

        // Validate data
        if (!raceId.isEmpty() && !participantId.isEmpty()) {
            // Create a new Registration object
            service.addRegistration(Integer.parseInt(raceId), Integer.parseInt(participantId));

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration added successfully.");

            // Clear text fields
            clearTextFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
        }
    }


    private void deleteRegistration() {
        String registrationId = registrationIdTextField.getText();
        if (!registrationId.isEmpty()) {
            boolean deleted = service.deleteRegistration(Integer.parseInt(registrationId));
            if (deleted) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Registration deleted successfully.");
                clearTextFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Registration not found.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a registration ID.");
        }
    }


    private void updateRegistration() {
        String registrationId = registrationIdTextField.getText();
        String raceId = raceIdTextField.getText();
        String participantId = participantIdTextField.getText();

        if (!registrationId.isEmpty() && !raceId.isEmpty() && !participantId.isEmpty()) {
            boolean updated = service.updateRegistration(Integer.parseInt(registrationId),Integer.parseInt(raceId),Integer.parseInt(participantId));
            if (updated) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Registration updated successfully.");
                clearTextFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Registration not found.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
        }
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app.fxml"));
            Parent root = loader.load();
            AppController appController = loader.getController();
            appController.initialize(service, username);
            appController.setService(service);
            appController.setUsername(username);


            Stage stage = new Stage();
            stage.setTitle("App");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the login window
            Stage loginStage = (Stage) backButton.getScene().getWindow();
            loginStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to go back.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearTextFields() {
        registrationIdTextField.clear();
        raceIdTextField.clear();
        participantIdTextField.clear();
    }
}

