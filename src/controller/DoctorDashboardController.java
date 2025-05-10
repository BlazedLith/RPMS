package controller;

import db.UserDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Doctor;
import model.Patient;
import model.UIHelper;

import java.io.IOException;

public class DoctorDashboardController {

    @FXML private TableView<Patient> patientsTable;
    @FXML private TableColumn<Patient, String> idCol, nameCol, emailCol;
    @FXML private Button logoutBtn;

    private Doctor doctor;

    // Called by login logic to pass in the logged-in doctor
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        // Load this doctor's assigned patients
        doctor.setAssignedPatients(UserDAO.getPatientsByDoctor(doctor.getUserId()));
        patientsTable.setItems(FXCollections.observableArrayList(doctor.getAssignedPatients()));
    }

    public void initialize() {
        // Bind table columns to Patient properties
        idCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    @FXML
    private void handleViewVitals() {
        openWindow("/DoctorPatientVitals.fxml", "View Patient Vitals");
    }

    @FXML
    private void handleGiveFeedback() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DoctorFeedbackForm.fxml"));
            Parent root = loader.load();
            // Pass doctor to feedback form
            loader.<DoctorFeedbackFormController>getController().setDoctor(doctor);
            showStage(root, "Give Feedback");
        } catch (IOException e) {
            UIHelper.showError("Failed to open feedback form.");
        }
    }

    @FXML
    private void handleManageAppointments() {
        openWindow("/DoctorAppointments.fxml", "Manage Appointments");
    }

    @FXML
    private void handleViewFeedbackHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DoctorFeedbackHistory.fxml"));
            Parent root = loader.load();
            loader.<DoctorFeedbackHistoryController>getController().setDoctor(doctor);
            showStage(root, "Feedback History");
        } catch (IOException e) {
            UIHelper.showError("Failed to load feedback history.");
        }
    }

    @FXML
    private void handleViewTrendsForSelectedPatient() {
        Patient selected = patientsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UIHelper.showError("Please select a patient first.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VitalsTrendView.fxml"));
            Parent root = loader.load();
            loader.<VitalsTrendViewController>getController().setPatient(selected);
            showStage(root, "Vitals Trends for " + selected.getName());
        } catch (IOException e) {
            UIHelper.showError("Failed to load health trends.");
        }
    }

    @FXML
    private void handleCommunicationRequests() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DoctorCommunicationRequests.fxml"));
            Parent root = loader.load();
            loader.<DoctorCommunicationRequestsController>getController().setDoctor(doctor);
            showStage(root, "Pending Requests");
        } catch (Exception e) {
            UIHelper.showError("Failed to load requests.");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Utility to open a new window without extra boilerplate
    private void openWindow(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            showStage(root, title);
        } catch (IOException e) {
            UIHelper.showError("Failed to open " + title + ".");
        }
    }

    // Utility to display a stage
    private void showStage(Parent root, String title) {
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }
}
