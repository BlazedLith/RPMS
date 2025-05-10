package controller;

import db.FeedbackDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Feedback;
import model.Patient;

import java.util.List;

public class FeedbackTableViewController {

    @FXML private TableView<Feedback> feedbackTable;
    @FXML private TableColumn<Feedback, String> doctorIdCol;
    @FXML private TableColumn<Feedback, String> dateCol;
    @FXML private TableColumn<Feedback, String> commentsCol;
    @FXML private TableColumn<Feedback, String> medicationsCol;

    private Patient patient;

    // Set the patient context and load their feedback
    public void setPatient(Patient patient) {
        this.patient = patient;
        loadFeedback();
    }

    public void initialize() {
        // Map columns to Feedback getters
        doctorIdCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        commentsCol.setCellValueFactory(new PropertyValueFactory<>("comments"));
        medicationsCol.setCellValueFactory(new PropertyValueFactory<>("medications"));
    }

    private void loadFeedback() {
        if (patient == null) return;
        List<Feedback> list = FeedbackDAO.getFeedbackByPatientId(patient.getUserId());
        ObservableList<Feedback> data = FXCollections.observableArrayList(list);
        feedbackTable.setItems(data);
    }
}
