package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.*;
import db.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class PatientDashboardController {

    private Patient patient;
    private Stage primaryStage;
    @FXML private Button logoutBtn;

    // Called after login to set the patient and stage context
    public void setPatient(Patient patient, Stage stage) {
        this.patient = patient;
        this.primaryStage = stage;
    }

    @FXML
    private void handleUploadVitals() {
        // Open the vitals upload screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VitalsUpload.fxml"));
            Parent root = loader.load();

            controller.VitalsUploadController ctrl = loader.getController();
            ctrl.setPatient(patient);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Upload Vitals");
            stage.setWidth(600);
            stage.show();
            SystemLogger.log("Vitals upload opened for patient: " + patient.getUserId());
        } catch (Exception e) {
            UIHelper.showError("Failed to open vitals upload screen.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewAppointments() {
        // Show patient's appointments
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AppointmentsTableView.fxml"));
            Parent root = loader.load();

            controller.AppointmentsTableViewController ctrl = loader.getController();
            ctrl.setPatient(patient);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Appointments");
            stage.show();
        } catch (Exception e) {
            UIHelper.showError("Failed to open appointments view.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewVitals() {
        // Display vitals history
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VitalsTableView.fxml"));
            Parent root = loader.load();

            controller.VitalsTableViewController ctrl = loader.getController();
            ctrl.setPatient(patient);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("View Vitals History");
            stage.show();
        } catch (Exception e) {
            UIHelper.showError("Failed to open vitals history view.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewFeedback() {
        // Display feedback from doctors
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FeedbackTableView.fxml"));
            Parent root = loader.load();

            controller.FeedbackTableViewController ctrl = loader.getController();
            ctrl.setPatient(patient);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Feedback from Doctor");
            stage.show();
        } catch (Exception e) {
            UIHelper.showError("Failed to open feedback view.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleScheduleAppointment() {
        // Open appointment scheduling form
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RequestAppointment.fxml"));
            Parent root = loader.load();

            RequestAppointmentController ctrl = loader.getController();
            ctrl.setPatient(patient);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Schedule Appointment");
            stage.show();
            SystemLogger.log("Appointment request opened for patient: " + patient.getUserId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewHealthTrends() {
        // Show graph of vitals trends
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VitalsTrendView.fxml"));
            Parent root = loader.load();

            VitalsTrendViewController ctrl = loader.getController();
            ctrl.setPatient(patient);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Health Trends");
            stage.show();
        } catch (IOException e) {
            UIHelper.showError("Failed to load health trends.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGenerateReport() {
        // Export vitals and feedback to CSV report
        try {
            List<Vital> vitals = VitalsDAO.getVitalsByPatientId(patient.getUserId());
            List<Feedback> feedbackList = FeedbackDAO.getFeedbackByPatientId(patient.getUserId());

            File file = new File("HealthReport_" + patient.getUserId() + ".csv");
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("Vitals History:");
                writer.println("Date,Heart Rate,Oxygen Level,Temperature,Blood Pressure");
                for (Vital v : vitals) {
                    writer.printf("%s,%.2f,%.2f,%.2f,%.2f%n",
                            v.getTimestamp(), v.getHeartRate(), v.getOxygenLevel(),
                            v.getTemperature(), v.getBloodPressure());
                }
                writer.println("\nDoctor Feedback:");
                for (Feedback f : feedbackList) {
                    writer.printf("%s | %s%n", f.getDate(), f.getComments());
                }
            }
            UIHelper.showInfo("Health report saved to:\n" + file.getAbsolutePath());
        } catch (Exception e) {
            UIHelper.showError("Failed to generate report.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRequestCommunication() {
        // Open communication request form
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PatientRequestCommunication.fxml"));
            Parent root = loader.load();

            PatientRequestCommunicationController ctrl = loader.getController();
            ctrl.setPatient(patient);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Communication Request");
            stage.show();
        } catch (Exception e) {
            UIHelper.showError("Failed to open request window.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        // Return to login screen
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
}
