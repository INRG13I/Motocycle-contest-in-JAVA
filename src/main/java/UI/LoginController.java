package UI;



import Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    private Service service;

    private String username;

    public void setService(Service service) {
        this.service = service;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    public void initialize(Service service) {
        setService(service);
        // Initialize method is called after loading the FXML file
        loginButton.setOnAction(this::login);
        registerButton.setOnAction(this::register);
    }

    private void login(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (service.validateLogin(username, password)) {
            openConfirmationWindow();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private void register(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (!service.validateLogin(username, password)) {
            service.addOrganizer(usernameField.getText(), passwordField.getText());
            showAlert(Alert.AlertType.INFORMATION,"Registration Successful", "You have been registered successfully. Please log in.");
        } else {
            showAlert(Alert.AlertType.ERROR,"Registration Error", "Organizer already exists. Please choose a different username.");
        }
    }

    private void openConfirmationWindow() {
        try {
            setUsername(usernameField.getText());
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
            Stage loginStage = (Stage) loginButton.getScene().getWindow();
            loginStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,"Error", "Failed to open confirmation window.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
