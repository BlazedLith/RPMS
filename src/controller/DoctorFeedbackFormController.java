package controller;

import db.FeedbackDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Doctor;
import model.Feedback;
import model.UIHelper;
import model.SystemLogger;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DoctorFeedbackFormController {

    @FXML private TextField patientIdField;   // Input for patient ID
    @FXML private TextArea commentsArea;      // Input for doctor's comments
    @FXML private TextField medsField;        // Input for comma-separated medications

    private Doctor doctor;                     // Logged-in doctor

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @FXML
    private void handleSubmit() {
        String patientId = patientIdField.getText().trim();
        String comments  = commentsArea.getText().trim();
        String medsText  = medsField.getText().trim();

        // Validate required fields
        if (patientId.isEmpty() || comments.isEmpty()) {
            UIHelper.showError("Please fill in all fields.");
            return;
        }

        // Parse medications into a list
        List<String> meds = Arrays.asList(medsText.split(",\\s*"));

        // Create feedback record
        Feedback feedback = new Feedback(
                UUID.randomUUID().toString(),
                doctor.getUserId(),
                patientId,
                LocalDateTime.now(),
                comments,
                meds
        );

        // Save and log outcome
        if (FeedbackDAO.saveFeedback(feedback)) {
            UIHelper.showSuccess("Feedback submitted.");
            SystemLogger.log("Feedback by doctor " + doctor.getUserId() + " for patient " + patientId);
        } else {
            UIHelper.showError("Failed to save feedback.");
        }
    }
}
