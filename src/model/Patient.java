package model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import controller.PatientDashboardController;

public class Patient extends User {

    private List<Vital> vitalsHistory;
    private List<Appointment> appointmentHistory;
    private List<Feedback> feedbackHistory;

    // Constructor
    public Patient(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.vitalsHistory = new ArrayList<>();
        this.appointmentHistory = new ArrayList<>();
        this.feedbackHistory = new ArrayList<>();
    }
    
    @Override
    public void displayDashboard(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PatientDashboard.fxml"));
            Parent root = loader.load();

            PatientDashboardController controller = loader.getController();
            controller.setPatient(this, primaryStage);

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Patient Dashboard");
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Getters and Setters
    public void addVital(Vital vital) {
        vitalsHistory.add(vital);
    }

    public List<Vital> getVitalsHistory() {
        return vitalsHistory;
    }

    public void addAppointment(Appointment appointment) {
        appointmentHistory.add(appointment);
    }

    public List<Appointment> getAppointmentHistory() {
        return appointmentHistory;
    }

    public void addDoctorFeedback(Feedback feedback) {
        feedbackHistory.add(feedback);
    }

    public List<Feedback> getDoctorFeedback() {
        return feedbackHistory;
    }
}
