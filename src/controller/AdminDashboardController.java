package controller;

import db.AdminDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Administrator;
import model.UIHelper;
import model.User;

public class AdminDashboardController {

    @FXML private TableView<User> usersTable;                       // Table showing all users
    @FXML private TableColumn<User,String> idCol, nameCol, emailCol; // Columns for user properties
    @FXML private Button logoutBtn;                                  // Button to log out

    private Administrator admin;                                     // Logged-in admin instance

    public void setAdmin(Administrator admin) {
        this.admin = admin;
    }

    public void initialize() {
        // Link table columns to User fields
        idCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        loadUsers();
    }

    private void loadUsers() {
        // Fetch users from DB and populate table
        ObservableList<User> data = FXCollections.observableArrayList(
                AdminDAO.getAllUsers()
        );
        usersTable.setItems(data);
    }

    @FXML private void handleDeleteUser() {
        User u = usersTable.getSelectionModel().getSelectedItem();
        if (u != null && AdminDAO.deleteUser(u.getUserId())) {
            UIHelper.showSuccess("Deleted user " + u.getName());
            loadUsers();
        } else {
            UIHelper.showError("Cannot delete user.");
        }
    }

    @FXML
    private void handleSystemLogs() {
        try {
            // Read log file and display in a scrollable dialog
            var logPath = java.nio.file.Paths.get("logs/system.log");
            String content = java.nio.file.Files.readString(logPath);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("System Logs");
            alert.setHeaderText("System Activity Log");
            TextArea textArea = new TextArea(content);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setPrefSize(600, 400);
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();
        } catch (Exception e) {
            UIHelper.showError("Failed to load system logs.");
            e.printStackTrace();
        }
    }

    @FXML
    private void ReportGenerator() {
        try {
            // Let admin pick a file to save the CSV report
            var fileChooser = new javafx.stage.FileChooser();
            fileChooser.setInitialFileName("system_report.csv");
            var file = fileChooser.showSaveDialog(null);
            if (file == null) return;

            try (var writer = new java.io.FileWriter(file)) {
                writer.write("Report Type,Count\n");
                writer.write("Total Users," + AdminDAO.getAllUsers().size() + "\n");
                writer.write("Vitals Uploaded," + db.VitalsDAO.getAllVitals().size() + "\n");
                writer.write("Appointments," + db.AppointmentDAO.getAllAppointments().size() + "\n");
                writer.write("Doctor Feedbacks," + db.FeedbackDAO.getAllFeedback().size() + "\n");

                UIHelper.showSuccess("System report saved to: " + file.getAbsolutePath());
            }

        } catch (Exception e) {
            UIHelper.showError("Failed to export system report.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddUser() {
        try {
            // Open the add-user form in a new window
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/AddUserForm.fxml")
            );
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add New User");
            stage.show();
        } catch (Exception e) {
            UIHelper.showError("Failed to open add user form.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            // Return to the login screen
            Parent root = FXMLLoader.load(
                    getClass().getResource("/Login.fxml")
            );
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
