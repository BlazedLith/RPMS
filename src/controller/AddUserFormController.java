package controller;

import db.AdminDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.UIHelper;

public class AddUserFormController {

    @FXML private TextField userIdField, nameField, emailField, doctorIdField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleBox;

    public void initialize() {
        // Set up role options
        roleBox.getItems().addAll("patient", "doctor", "admin");
    }

    @FXML
    private void handleCreateUser() {
        String userId   = userIdField.getText();
        String name     = nameField.getText();
        String email    = emailField.getText();
        String password = passwordField.getText();
        String role     = roleBox.getValue();
        String doctorId = doctorIdField.getText();

        // Ensure required fields are provided
        if (userId.isBlank() || name.isBlank() || email.isBlank() || password.isBlank() || role == null) {
            UIHelper.showError("Please fill all required fields.");
            return;
        }

        boolean success = AdminDAO.addUser(userId, name, email, password, role);
        if (success) {
            // Link patient to doctor if applicable
            if ("patient".equals(role) && !doctorId.isBlank()) {
                AdminDAO.linkPatientToDoctor(userId, doctorId);
            }
            UIHelper.showSuccess("User created successfully.");
        } else {
            UIHelper.showError("Failed to create user. Try another ID or check database.");
        }
    }
}
