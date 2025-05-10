package controller;

import db.FeedbackDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Doctor;
import model.Feedback;

import java.util.List;

public class DoctorFeedbackHistoryController {

    @FXML private TableView<Feedback> feedbackTable;
    @FXML private TableColumn<Feedback, String> patientIdCol;
    @FXML private TableColumn<Feedback, String> commentsCol;
    @FXML private TableColumn<Feedback, String> medicationsCol;
    @FXML private TableColumn<Feedback, String> dateCol;

    private Doctor doctor;

    // Called after FXML load to set the current doctor and fetch their feedback
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        loadFeedback();
    }

    public void initialize() {
        // Map columns to Feedback getters
        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        commentsCol.setCellValueFactory(new PropertyValueFactory<>("comments"));
        medicationsCol.setCellValueFactory(new PropertyValueFactory<>("formattedMedications"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
    }

    private void loadFeedback() {
        // Retrieve and display feedback for this doctor
        List<Feedback> list = FeedbackDAO.getFeedbackByDoctorId(doctor.getUserId());
        ObservableList<Feedback> data = FXCollections.observableArrayList(list);
        feedbackTable.setItems(data);
    }
}
