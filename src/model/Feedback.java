package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Feedback {
    private String feedbackId;
    private String doctorId;
    private String patientId;
    private LocalDateTime date;
    private String comments;
    private List<String> medications;

    public Feedback(String feedbackId, String doctorId, String patientId, LocalDateTime date, String comments, List<String> medications) {
        this.feedbackId = feedbackId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.date = date;
        this.comments = comments;
        this.medications = medications;
    }

    //Getters
    public String getFeedbackId() {
        return feedbackId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public String getComments() {
        return comments;
    }

    public List<String> getMedications() {
        return medications;
    }

    // Setters
    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public String getFormattedMedications() {
        return String.join(", ", medications);
    }
}
