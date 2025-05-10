package controller;

import db.CommunicationRequestDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.CommunicationRequest;
import model.Doctor;
import model.EmailService;
import model.UIHelper;
import db.UserDAO;

public class DoctorCommunicationRequestsController {

    @FXML private TableView<CommunicationRequest> requestTable;
    @FXML private TableColumn<CommunicationRequest, String> patientCol, typeCol, statusCol;
    @FXML private TextField linkField;

    private Doctor doctor;

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        loadRequests();
    }

    public void initialize() {
        // Map columns to request properties
        patientCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getPatientId()));
        typeCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));
        statusCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
    }

    private void loadRequests() {
        // Populate table with this doctor's requests
        requestTable.setItems(FXCollections.observableArrayList(
                CommunicationRequestDAO.getRequestsByDoctor(doctor.getUserId())
        ));
    }

    @FXML
    private void handleApprove() {
        CommunicationRequest req = requestTable.getSelectionModel().getSelectedItem();
        String link = linkField.getText();
        if (req == null || link.isBlank()) {
            UIHelper.showError("Select a request and enter a link.");
            return;
        }
        CommunicationRequestDAO.respondToRequest(req.getRequestId(), "approved", link);
        UIHelper.showSuccess("Request approved.");
        loadRequests();

        // Send email to patient about approval
        String patientEmail = UserDAO.getEmailByUserId(req.getPatientId());
        String subject = "Your " + req.getType() + " request has been approved";
        String body = "Dear Patient,\n\n"
                + "Your request for a " + req.getType() + " session has been approved.\n"
                + "Join using this link: " + linkField.getText() + "\n\n"
                + "Regards,\nRemote Patient Monitoring System";

        EmailService.sendEmail(patientEmail, subject, body);
    }

    @FXML
    private void handleReject() {
        CommunicationRequest req = requestTable.getSelectionModel().getSelectedItem();
        if (req == null) {
            UIHelper.showError("Select a request to reject.");
            return;
        }
        CommunicationRequestDAO.respondToRequest(req.getRequestId(), "rejected", "");
        UIHelper.showSuccess("Request rejected.");
        loadRequests();

        // Send email to patient about rejection
        String patientEmail = UserDAO.getEmailByUserId(req.getPatientId());
        String subject = "Your " + req.getType() + " request has been rejected";
        String body = "Dear Patient,\n\n"
                + "We regret to inform you that your request for a " + req.getType()
                + " session has been rejected by the doctor.\n\n"
                + "Regards,\nRemote Patient Monitoring System";

        EmailService.sendEmail(patientEmail, subject, body);
    }
}
