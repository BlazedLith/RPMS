package model;

import controller.DoctorDashboardController;
import db.UserDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class Doctor extends User {
    private List<Patient> assignedPatients;
    private List<Appointment> appointmentList;

    public Doctor(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.assignedPatients = UserDAO.getPatientsByDoctor(userId);
        this.appointmentList = new ArrayList<>();
    }

    public Feedback provideFeedback(String patientId, String comments, List<String> medications) {
        return new Feedback(UUID.randomUUID().toString(), this.userId, patientId, LocalDateTime.now(), comments, medications);
    }

    @Override
    public void displayDashboard(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DoctorDashboard.fxml"));
            Parent root = loader.load();

            DoctorDashboardController controller = loader.getController();
            controller.setDoctor(this);

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Doctor Dashboard");
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Getters and Setters
    public void addPatient(Patient patient) {
        assignedPatients.add(patient);
    }

    public void removePatient(Patient patient) {
        assignedPatients.remove(patient);
    }

    public void setAssignedPatients(List<Patient> assignedPatients) {
        this.assignedPatients = assignedPatients;
    }

    public List<Patient> getAssignedPatients() {
        return assignedPatients;
    }

    public void addAppointment(Appointment appointment) {
        appointmentList.add(appointment);
    }

    public void removeAppointment(Appointment appointment) {
        appointmentList.remove(appointment);
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

}
