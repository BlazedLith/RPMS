package controller;

import db.CommunicationRequestDAO;
import db.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import model.CommunicationRequest;
import model.Patient;
import model.UIHelper;

public class PatientRequestCommunicationController {

    @FXML private TextArea requestTextArea;  // Displays current communication requests
    private Patient patient;                  // Current logged-in patient

    // Called by dashboard to set patient context and load existing requests
    public void setPatient(Patient patient) {
        this.patient = patient;
        loadRequests();
    }

    // Populate the text area with formatted request statuses
    private void loadRequests() {
        StringBuilder sb = new StringBuilder();
        for (CommunicationRequest req : CommunicationRequestDAO.getRequestsByPatient(patient.getUserId())) {
            String label = req.getType().toUpperCase() + " - " + req.getStatus().toUpperCase();
            switch (req.getStatus().toLowerCase()) {
                case "approved":
                    label += " → " + req.getLink();
                    break;
                case "rejected":
                    label += " ❌";
                    break;
                default:
                    label += " ⏳";
                    break;
            }
            sb.append(label).append("\n\n");
        }
        requestTextArea.setText(sb.toString());
    }

    @FXML
    private void handleVideoRequest() {
        // Send a video call request to the assigned doctor
        String doctorId = UserDAO.getAssignedDoctorId(patient.getUserId());
        CommunicationRequestDAO.createRequest(patient.getUserId(), doctorId, "video");
        UIHelper.showSuccess("Video call request sent.");
        loadRequests();
    }

    @FXML
    private void handleChatRequest() {
        // Send a chat request to the assigned doctor
        String doctorId = UserDAO.getAssignedDoctorId(patient.getUserId());
        CommunicationRequestDAO.createRequest(patient.getUserId(), doctorId, "chat");
        UIHelper.showSuccess("Chat request sent.");
        loadRequests();
    }
}
