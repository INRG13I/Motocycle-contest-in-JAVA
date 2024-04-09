package UI;

import Domain.Participant;
import Service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ParticipantsController {

    @FXML
    private ListView<String> participantsListView;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField capacityTextField;

    @FXML
    private TextField teamTextField;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button findByIdButton;

    @FXML
    private Button backButton;

    Service service;
    String username;

    public void setService(Service service) {
        this.service = service;
    }
    public void setUsername(String username) {this.username = username;}

    @FXML
    void initialize(Service service,String username) {

        setService(service);
        setUsername(username);
        // Set up button actions
        addButton.setOnAction(event -> addParticipant());
        deleteButton.setOnAction(event -> deleteParticipant());
        updateButton.setOnAction(event -> updateParticipant());
        findByIdButton.setOnAction(event -> findParticipantById());
        backButton.setOnAction(event -> goBack());
        loadParticipants();
    }

    private void loadParticipants() {
        participantsListView.getItems().clear();
        Iterable<Participant> participants = service.findAllParticipant();
        for (Participant participant : participants) {
            participantsListView.getItems().add(participant.toString());
        }
    }

    private void addParticipant() {
        String name = nameTextField.getText();
        String capacity = capacityTextField.getText();
        String team = teamTextField.getText();
        if (!name.isEmpty() && !capacity.isEmpty() && !team.isEmpty()) {
            service.addParticipant(name, Integer.parseInt(capacity), team);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Participant added successfully.");
            loadParticipants();
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
        }
    }

    private void deleteParticipant() {
        String id = idTextField.getText();
        if (!id.isEmpty()) {
            boolean deleted = service.deleteParticipant(Integer.parseInt(id));
            if (deleted) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Participant deleted successfully.");
                loadParticipants();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Participant not found.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter an ID.");
        }
    }

    private void updateParticipant() {
        String id = idTextField.getText();
        String name = nameTextField.getText();
        String capacity = capacityTextField.getText();
        String team = teamTextField.getText();
        if (!name.isEmpty() && !capacity.isEmpty() && !team.isEmpty()) {
            boolean updated = service.updateParticipant(Integer.parseInt(id), name, Integer.parseInt(capacity), team);
            if (updated) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Participant updated successfully.");
                loadParticipants();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Participant not found.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
        }
    }

    private void findParticipantById() {
        String id = idTextField.getText();
        if (!id.isEmpty()) {
            Participant participant = service.findParticipantById(Integer.parseInt(id));
            if (participant != null) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Participant found: " + participant);
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Participant not found.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter an ID.");
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
            showAlert(Alert.AlertType.ERROR,"Error", "Failed to go back.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nameTextField.clear();
        capacityTextField.clear();
        teamTextField.clear();
        idTextField.clear();
    }
}

