package model;

public class CommunicationRequest {
    private String requestId;
    private String patientId;
    private String doctorId;
    private String type;
    private String status;
    private String link;

    public CommunicationRequest(String requestId, String patientId, String doctorId, String type, String status, String link) {
        this.requestId = requestId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.type = type;
        this.status = status;
        this.link = link;
    }

    // Getters and setters
    public String getRequestId() { return requestId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getLink() { return link; }

    public void setStatus(String status) { this.status = status; }
    public void setLink(String link) { this.link = link; }
}
