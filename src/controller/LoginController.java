package controller;

import db.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.SystemLogger;
import model.User;
import model.UIHelper;

public class LoginController {

    @FXML private TextField emailField;         // User's email input
    @FXML private PasswordField passwordField;  // User's password input

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Email and password cannot be empty.");
            return;
        }

        User user = UserDAO.getUserByEmailAndPassword(email, password);
        if (user != null) {
            UIHelper.showInfo("Login successful: " + user.getName());
            user.displayDashboard(new Stage());         // Open appropriate dashboard
            emailField.getScene().getWindow().hide();   // Close login window
            SystemLogger.log("Login: " + user.getUserId()
                    + " (" + user.getClass().getSimpleName() + ")");
        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
