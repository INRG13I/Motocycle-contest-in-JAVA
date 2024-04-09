package UI;

import Domain.Race;
import Service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RaceController {

    @FXML
    private ListView<String> racesListView;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField capacityTextField;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

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
        setUsername(username);
        addButton.setOnAction(event -> addRace());
        deleteButton.setOnAction(event -> deleteRace());
        backButton.setOnAction(event -> goBack());
        loadRaces();
    }

    private void loadRaces() {
        racesListView.getItems().clear();
        Iterable<Race> races = service.findAllRace();
        for (Race race : races) {
            racesListView.getItems().add(race.toString());
        }
    }

    private void addRace() {
        String id = idTextField.getText();
        String capacity = capacityTextField.getText();
        if (!id.isEmpty() && !capacity.isEmpty()) {
            service.addRace(Integer.parseInt(capacity));
            showAlert(Alert.AlertType.INFORMATION, "Success", "Race added successfully.");
            loadRaces();
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
        }
    }

    private void deleteRace() {
        String id = idTextField.getText();
        if (!id.isEmpty()) {
            boolean deleted = service.deleteRace(Integer.parseInt(id));
            if (deleted) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Race deleted successfully.");
                loadRaces();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Race not found.");
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
        idTextField.clear();
        capacityTextField.clear();
    }
}
